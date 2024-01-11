package com.consoleCRUDApp.repository.hibernate;

import com.consoleCRUDApp.model.Label;
import com.consoleCRUDApp.model.Post;
import com.consoleCRUDApp.model.PostStatus;
import com.consoleCRUDApp.model.Status;
import com.consoleCRUDApp.repository.LabelRepository;
import com.consoleCRUDApp.repository.PostRepository;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static com.consoleCRUDApp.repository.hibernate.HibernateUtil.getSessionFactory;
import static com.consoleCRUDApp.repository.hibernate.SQLQueries.*;

@AllArgsConstructor
public class HibPostRepositoryImpl implements PostRepository {

    private final LabelRepository labelRepository;

    @Override
    public Optional<Post> save(Post post) {
        getSessionFactory().inTransaction(session -> {
            post.getLabels().replaceAll(label -> {
                Optional<Label> existingLabel = labelRepository.getLabelByName(label.getName());
                return existingLabel.orElseGet(() -> {
                    session.persist(label);
                    return label;
                });
            });
            session.persist(post);
        });
        return Optional.of(post);
    }

    @Override
    public List<Post> findAll() {
        List<Post> posts = new ArrayList<>();
        getSessionFactory().inTransaction(session ->
                posts.addAll(session.createSelectionQuery(HQL_FROM_POST_WHERE_STATUS, Post.class)
                        .setParameter("status", Status.ACTIVE)
                        .getResultList()));
        return posts;
    }

    @Override
    public Optional<Post> findById(Long id) {
        AtomicReference<Post> postAtomicReference = new AtomicReference<>();
        getSessionFactory().inTransaction(session ->
                session.createQuery(HQL_FROM_POST_WHERE_ID_AND_STATUS, Post.class)
                        .setParameter("id", id)
                        .setParameter("status", Status.ACTIVE)
                        .uniqueResultOptional()
                        .ifPresent(postAtomicReference::set));

        return Optional.ofNullable(postAtomicReference.get());
    }

    public Optional<Post> update(Post post) {
        getSessionFactory().inTransaction(session -> {
            Post existingPost = session.find(Post.class, post.getId());
            existingPost.setContent(post.getContent());
            existingPost.setUpdated(post.getUpdated());
            existingPost.setPostStatus(post.getPostStatus());
        });

        updateLabelsAndPostLabelLinks(post);

        return Optional.of(post);
    }

    private void updateLabelsAndPostLabelLinks(Post post) {
        getSessionFactory().inTransaction(session -> {

            // Remove existing links from post_label
            session.createNativeQuery(SQL_UPDATE_POST_LABEL_SET_STATUS_BY_POST_ID, Object.class)
                    .setParameter("status", String.valueOf(Status.DELETED))
                    .setParameter("postId", post.getId())
                    .executeUpdate();

            // Save new labels if not exist
            for (Label label : post.getLabels()) {
                Optional<Label> existingLabel = labelRepository.getLabelByName(label.getName());
                Label managedLabel;

                if (existingLabel.isPresent()) {
                    managedLabel = existingLabel.get();
                } else {
                    managedLabel = labelRepository.save(label).orElseThrow();
                    session.flush();
                }

                // Insert new links into post_label
                session.createNativeQuery(SQL_INSERT_INTO_POST_LABEL, Object.class)
                        .setParameter("postId", post.getId())
                        .setParameter("labelId", managedLabel.getId())
                        .setParameter("status", String.valueOf(Status.ACTIVE))
                        .executeUpdate();
            }
        });
    }

    @Override
    public boolean deleteById(Long id) {
        AtomicBoolean isDeleted = new AtomicBoolean(false);

        getSessionFactory().inTransaction(session -> {
            int affectedRows = session.createMutationQuery(HQL_UPDATE_POST_SET_STATUS_POST_STATUS_WHERE_ID)
                    .setParameter("status", Status.DELETED)
                    .setParameter("postStatus", PostStatus.DELETED)
                    .setParameter("id", id)
                    .executeUpdate();
            isDeleted.set(affectedRows > 0);
        });

        return isDeleted.get();
    }
}
