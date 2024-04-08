package dev.annyni.model;

import dev.annyni.model.Post;
import lombok.*;

import java.util.List;

/**
 * todo Document type Writer
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Writer {

    private Long id;
    private String firstName;
    private String lastName;
    private List<Post> posts;
    private Status status;
}
