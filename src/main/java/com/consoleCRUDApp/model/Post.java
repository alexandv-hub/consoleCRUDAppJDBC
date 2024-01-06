package com.consoleCRUDApp.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.WhereJoinTable;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(of = {"id", "content", "created", "updated", "postStatus", "status"})
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder

@Entity
@Table(name = "post")
public class Post implements DBEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime created;

    @Column
    private LocalDateTime updated;

    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(
            name = "post_label",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "label_id")
    )
    @WhereJoinTable(clause = "status = 'ACTIVE'")
    private List<Label> labels;

    @Column(name = "post_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PostStatus postStatus;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;
}
