package com.consoleCRUDApp.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@ToString
@Builder
public class Post implements Entity {
    private Long id;
    private String content;
    private LocalDateTime created;
    private LocalDateTime updated;
    private List<Label> labels;
    private PostStatus postStatus;
    private Status status;
}
