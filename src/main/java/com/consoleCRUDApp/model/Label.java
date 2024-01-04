package com.consoleCRUDApp.model;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@Builder
@ToString
public class Label implements Entity {
    private Long id;
    private String name;
    private Status status;
}
