package dev.annyni.model;

import lombok.*;

import java.util.Date;
import java.util.List;

/**
 * todo Document type Post
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    private Long id;
    private String content;
    private Date created;
    private Date updated;
    private List<Label> labels;
    private Status status;
    private Writer writer;
}
