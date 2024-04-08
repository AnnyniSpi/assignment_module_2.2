package dev.annyni.controller;

import dev.annyni.model.Post;
import dev.annyni.service.PostService;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * todo Document type PostController
 */
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;


    public Post getPostById(Long postId){
        return postService.getPostById(postId);
    }

    public List<Post> getAllPosts(){
        return postService.getAllPosts();
    }

    public Post createPost(Post post){
        return postService.createPost(post);
    }

    public Post updatePost(Post post){
        return postService.updatePost(post);
    }

    public void deletePostById(Long postId){
        postService.deletePostById(postId);
    }
}
