package com.consoleCRUDApp.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.*;

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
@SQLRestriction(value = "status <> 'DELETED'")
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

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "post_label",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "label_id"),
            uniqueConstraints = {@UniqueConstraint(columnNames = {"post_id", "label_id"})}
    )
    @SQLJoinTableRestriction("status = 'ACTIVE'")
    @SQLDeleteAll(sql = "UPDATE post_label SET status = 'DELETED' WHERE post_id = ?")
    @SQLInsert(sql = "INSERT INTO post_label " +
            "(post_id, label_id, status) VALUES(?, ?, 'ACTIVE') " +
            "ON CONFLICT (post_id, label_id) DO UPDATE SET status = 'ACTIVE'")
    private List<Label> labels;

    @Column(name = "post_status", nullable = false)
    @Enumerated(EnumType.STRING)
    @ColumnTransformer(write = "?::post_status_type")
    private PostStatus postStatus;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @ColumnTransformer(write = "?::status_type")
    private Status status;
}
