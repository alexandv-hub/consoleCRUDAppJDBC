package com.consoleCRUDApp.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.WhereJoinTable;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString

@Entity
@Table(name = "writer")
public class Writer implements DBEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(
            name = "writer_post",
            joinColumns = @JoinColumn(name = "writer_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id")
    )
    @WhereJoinTable(clause = "status = 'ACTIVE'")
    private List<Post> posts;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;
}
