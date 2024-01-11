package com.consoleCRUDApp.repository.hibernate;

import com.consoleCRUDApp.model.Post;
import com.consoleCRUDApp.model.Status;
import com.consoleCRUDApp.model.Writer;
import com.consoleCRUDApp.repository.PostRepository;
import com.consoleCRUDApp.repository.WriterRepository;
import lombok.AllArgsConstructor;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static com.consoleCRUDApp.repository.hibernate.HibernateUtil.getSessionFactory;
import static com.consoleCRUDApp.repository.hibernate.SQLQueries.*;


@AllArgsConstructor
public class HibWriterRepositoryImpl implements WriterRepository {

    private final PostRepository postRepository;

    @Override
    public Optional<Writer> save(Writer writer) {
        getSessionFactory().inTransaction(session -> {
            if (writer.getPosts() != null) {
                for (Post post : writer.getPosts()) {
                    postRepository.save(post);
                    session.flush();
                }
            }
            session.persist(writer);
        });
        return Optional.of(writer);
    }

    @Override
    public Optional<Writer> findById(Long id) {
        AtomicReference<Writer> postAtomicReference = new AtomicReference<>();

        getSessionFactory().inTransaction(session ->
                session.createQuery(HQL_FROM_WRITER_WHERE_ID_AND_STATUS, Writer.class)
                        .setParameter("id", id)
                        .setParameter("status", Status.ACTIVE)
                        .uniqueResultOptional()
                        .ifPresent(postAtomicReference::set)
        );

        return Optional.ofNullable(postAtomicReference.get());
    }

    @Override
    public List<Writer> findAll() {
        List<Writer> writers = new ArrayList<>();
        getSessionFactory().inTransaction(session ->
                writers.addAll(session.createSelectionQuery(HQL_FROM_WRITER_WHERE_STATUS, Writer.class)
                        .setParameter("status", Status.ACTIVE)
                        .getResultList()));
        return writers;
    }

    @Override
    public Optional<Writer> update(Writer writer) {
        getSessionFactory().inTransaction(session -> {
            Writer existingWriter = session.find(Writer.class, writer.getId());
            existingWriter.setFirstName(writer.getFirstName());
            existingWriter.setLastName(writer.getLastName());
        });

        updatePostsAndWriterPostLinks(writer);

        return Optional.of(writer);
    }

    private void updatePostsAndWriterPostLinks(Writer writer) {
        getSessionFactory().inTransaction(session -> {

            // Remove existing links from writer_post
            session.createNativeQuery(SQL_UPDATE_WRITER_POST_SET_STATUS_BY_POST_ID, Object.class)
                    .setParameter("status", String.valueOf(Status.DELETED))
                    .setParameter("writerId", writer.getId())
                    .executeUpdate();

            // Update existing or save new posts
            for (Post currPost : writer.getPosts()) {
                Optional<Post> existingPostOptional = postRepository.findById(currPost.getId());

                if (existingPostOptional.isPresent()) {
                    Post existingPost = existingPostOptional.get();

                    boolean isPostFieldsUpdated = !Objects.equals(currPost, existingPost);
                    boolean areLabelListsEqual = currPost.getLabels().size() == existingPost.getLabels().size()
                            && new HashSet<>(currPost.getLabels()).containsAll(existingPost.getLabels());

                    if (isPostFieldsUpdated || areLabelListsEqual) {
                        postRepository.update(currPost);
                    }
                } else {
                    postRepository.save(currPost);
                }

                // Insert new links into writer_post
                session.createNativeQuery(SQL_INSERT_INTO_WRITER_POST, Object.class)
                        .setParameter("writerId", writer.getId())
                        .setParameter("postId", currPost.getId())
                        .setParameter("status", String.valueOf(Status.ACTIVE))
                        .executeUpdate();
            }
        });
    }

    @Override
    public boolean deleteById(Long id) {
        AtomicBoolean isDeleted = new AtomicBoolean(false);

        getSessionFactory().inTransaction(session -> {
            int affectedRows = session.createMutationQuery(HQL_UPDATE_WRITER_SET_STATUS_WHERE_ID)
                    .setParameter("status", Status.DELETED)
                    .setParameter("id", id)
                    .executeUpdate();
            isDeleted.set(affectedRows > 0);
        });

        return isDeleted.get();
    }
}
