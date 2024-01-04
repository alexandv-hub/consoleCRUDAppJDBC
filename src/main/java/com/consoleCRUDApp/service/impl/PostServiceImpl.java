package com.consoleCRUDApp.service.impl;

import com.consoleCRUDApp.model.Post;
import com.consoleCRUDApp.repository.PostRepository;
import com.consoleCRUDApp.service.PostService;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class PostServiceImpl implements PostService {

    PostRepository postRepository;

    @Override
    public Optional<Post> save(Post post) {
        return postRepository.save(post);
    }

    @Override
    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }

    @Override
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @Override
    public Optional<Post> update(Post post) {
        return postRepository.update(post);
    }

    @Override
    public boolean deleteById(Long id) {
        return postRepository.deleteById(id);
    }
}
