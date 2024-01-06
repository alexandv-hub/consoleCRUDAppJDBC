package com.consoleCRUDApp.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(of = {"id", "name", "status"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString

@Entity
@Table(name = "label")
public class Label implements DBEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;
}
