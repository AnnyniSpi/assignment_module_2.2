package service;

import dev.annyni.model.Post;
import dev.annyni.model.Status;
import dev.annyni.repository.PostRepository;
import dev.annyni.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * todo Document type PostServiceTest
 */
public class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    private PostService postService;

    private Post testPost;

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);

        postService = new PostService(postRepository);

        testPost = Post.builder()
            .id(1L)
            .content("test content")
            .created(new Date())
            .updated(new Date())
            .status(Status.ACTIVE)
            .build();
    }

    @Test
    void findByIdTest(){
        Mockito.when(postRepository.findById(anyLong())).thenReturn(testPost);

        Post post = postService.getPostById(1L);

        assertNotNull(post);
        assertEquals("test content", post.getContent());
        assertEquals(Status.ACTIVE, post.getStatus());
    }

    @Test
    void findAllTest(){
        List<Post> posts = new ArrayList<>();
        posts.add(testPost);

        Mockito.when(postRepository.findAll()).thenReturn(posts);

        List<Post> postList = postService.getAllPosts();

        assertNotNull(postList);
        assertEquals(1, postList.size());
        assertEquals("test content", postList.get(0).getContent());
        assertEquals(Status.ACTIVE, postList.get(0).getStatus());
    }

    @Test
    void createTest(){
        Mockito.when(postRepository.save(any(Post.class))).thenReturn(testPost);

        Post post = postService.createPost(testPost);

        assertNotNull(post);
        assertEquals("Test content", post.getContent());
        assertEquals(Status.ACTIVE, post.getStatus());

        verify(postRepository, times(1)).save(testPost);
    }

    @Test
    void updateTest(){
        Post updatePost = Post.builder()
            .id(1L)
            .content("update content")
            .updated(new Date())
            .status(Status.ACTIVE)
            .build();

        Mockito.when(postRepository.update(any(Post.class))).thenReturn(updatePost);

        Post post = postService.updatePost(updatePost);

        assertNotNull(post);
        assertEquals("update content", post.getContent());
        assertEquals(Status.ACTIVE, post.getStatus());

        verify(postRepository, times(1)).update(updatePost);
    }

    @Test
    void deleteTest(){
        postService.deletePostById(1L);
        verify(postRepository, times(1)).deleteById(1L);
    }


}
