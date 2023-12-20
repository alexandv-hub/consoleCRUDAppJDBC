package com.consoleCRUDApp.service;

import com.consoleCRUDApp.model.Post;
import com.consoleCRUDApp.repository.PostRepository;
import lombok.AllArgsConstructor;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class PostServiceImpl implements PostService {

    PostRepository postRepository;

    @Override
    public Post save(Post post) throws SQLException {
        return postRepository.save(post);
    }

    @Override
    public Optional<Post> findById(Long id) throws SQLException {
        return postRepository.findById(id);
    }

    @Override
    public List<Post> findAll() throws SQLException {
        return postRepository.findAll();
    }

    @Override
    public Optional<Post> update(Post post) throws SQLException {
        return postRepository.update(post);
    }

    @Override
    public boolean deleteById(Long id) throws SQLException {
        return postRepository.deleteById(id);
    }

    @Override
    public Class<Post> getEntityClass() {
        return postRepository.getEntityClass();
    }
}
