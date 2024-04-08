package dev.annyni.service;

import dev.annyni.model.Post;
import dev.annyni.repository.PostRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * todo Document type PostService
 */
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public Post getPostById(Long postId){
        return postRepository.findById(postId);
    }

    public List<Post> getAllPosts(){
        return postRepository.findAll();
    }

    public Post createPost(Post post){
        return postRepository.save(post);
    }

    public Post updatePost(Post post){
        return postRepository.update(post);
    }

    public void deletePostById(Long postId){
        postRepository.deleteById(postId);
    }
}
