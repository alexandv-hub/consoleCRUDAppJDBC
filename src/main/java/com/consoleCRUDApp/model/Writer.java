package com.consoleCRUDApp.model;

import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;

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
@SQLRestriction(value = "status <> 'DELETED'")
public class Writer implements DBEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "writer_post",
            joinColumns = @JoinColumn(name = "writer_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id"),
            uniqueConstraints = {@UniqueConstraint(columnNames = {"writer_id", "post_id"})}
    )
    @SQLJoinTableRestriction("status = 'ACTIVE'")
    @SQLDeleteAll(sql = "UPDATE writer_post SET status = 'DELETED' WHERE writer_id = ?")
    @SQLInsert(sql = "INSERT INTO writer_post " +
            "(writer_id, post_id, status) VALUES(?, ?, 'ACTIVE') " +
            "ON CONFLICT (writer_id, post_id) DO UPDATE SET status = 'ACTIVE'")
    private List<Post> posts;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @ColumnTransformer(write = "?::status_type")
    private Status status;
}
