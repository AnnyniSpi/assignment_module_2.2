package dev.annyni.model;

import lombok.*;

/**
 * todo Document type Label
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Label {
    private Long id;
    private String name;
    private Status status;
}
