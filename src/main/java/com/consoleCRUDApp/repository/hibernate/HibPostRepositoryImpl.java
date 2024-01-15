package com.consoleCRUDApp.repository.hibernate;

import com.consoleCRUDApp.model.Post;
import com.consoleCRUDApp.model.PostStatus;
import com.consoleCRUDApp.model.Status;
import com.consoleCRUDApp.repository.PostRepository;
import lombok.AllArgsConstructor;
import org.hibernate.Hibernate;

import java.util.List;
import java.util.Optional;

import static com.consoleCRUDApp.repository.hibernate.HibernateUtil.getSessionFactory;
import static com.consoleCRUDApp.repository.hibernate.SQLQueries.HQL_FROM_POST_LEFT_JOIN_FETCH_LABELS;

@AllArgsConstructor
public class HibPostRepositoryImpl implements PostRepository {

    @Override
    public Optional<Post> save(Post post) {
        getSessionFactory().inTransaction(session ->
                session.persist(post));
        return Optional.of(post);
    }

    @Override
    public List<Post> findAll() {
        return getSessionFactory().fromTransaction(session ->
                session.createSelectionQuery(HQL_FROM_POST_LEFT_JOIN_FETCH_LABELS, Post.class)
                        .getResultList());
    }

    @Override
    public Optional<Post> findById(Long id) {
        return Optional.ofNullable(getSessionFactory().fromTransaction(session -> {
            Post existingPost = session.find(Post.class, id);
            if (existingPost != null) {
                Hibernate.initialize(existingPost.getLabels());
            }
            return existingPost;
        }));
    }

    @Override
    public Optional<Post> update(Post post) {
        return Optional.ofNullable(getSessionFactory().fromTransaction(session ->
                session.merge(post)));
    }

    @Override
    public boolean deleteById(Long id) {
        getSessionFactory().inTransaction(session -> {
            Post post = session.find(Post.class, id);
            if (post != null) {
                post.setStatus(Status.DELETED);
                post.setPostStatus(PostStatus.DELETED);
                session.merge(post);
            }
        });
        return true;
    }
}
