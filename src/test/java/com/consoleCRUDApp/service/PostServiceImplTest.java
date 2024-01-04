package com.consoleCRUDApp.service;

import com.consoleCRUDApp.model.Post;
import com.consoleCRUDApp.repository.PostRepository;
import com.consoleCRUDApp.service.impl.PostServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostServiceImpl postService;

    @Test
    void testSavePost() {
        Post post = Post.builder().build();
        when(postRepository.save(any(Post.class))).thenReturn(Optional.ofNullable(post));

        Optional<Post> savedPost = postService.save(post);

        assertNotNull(savedPost);
        verify(postRepository).save(post);
    }

    @Test
    void testFindById() {
        Long id = 1L;
        Post post = Post.builder().build();
        when(postRepository.findById(id)).thenReturn(Optional.of(post));

        Optional<Post> foundPost = postService.findById(id);

        assertTrue(foundPost.isPresent());
        assertEquals(post, foundPost.get());
        verify(postRepository).findById(id);
    }

    @Test
    void testFindAll() {
        Post post = Post.builder().build();
        when(postRepository.findAll()).thenReturn(Collections.singletonList(post));

        List<Post> posts = postService.findAll();

        assertFalse(posts.isEmpty());
        assertEquals(1, posts.size());
        verify(postRepository).findAll();
    }

    @Test
    void testUpdatePost() {
        Post post = Post.builder().build();
        when(postRepository.update(post)).thenReturn(Optional.of(post));

        Optional<Post> updatedPost = postService.update(post);

        assertTrue(updatedPost.isPresent());
        verify(postRepository).update(post);
    }

    @Test
    void testDeleteById() {
        Long id = 1L;
        when(postRepository.deleteById(id)).thenReturn(true);

        boolean isDeleted = postService.deleteById(id);

        assertTrue(isDeleted);
        verify(postRepository).deleteById(id);
    }

}
