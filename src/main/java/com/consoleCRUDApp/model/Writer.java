package com.consoleCRUDApp.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@Builder
@ToString
public class Writer implements Entity {
    private Long id;
    private String firstName;
    private String lastName;
    private List<Post> posts;
    private Status status;
}
