package com.consoleCRUDApp.model;

import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.*;

@Getter
@Setter
@EqualsAndHashCode(of = {"id", "name", "status"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString

@Entity
@Table(name = "label")
@SQLRestriction("status <> 'DELETED'")
@SQLDelete(sql = "update Label set status = 'DELETED' where id = ?")
@SQLInsert(sql = "INSERT INTO label (name, status) VALUES (?, ?::status_type) " +
                 "ON CONFLICT (name) DO UPDATE SET name = excluded.name RETURNING id")
public class Label implements DBEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NaturalId(mutable = true)
    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @ColumnTransformer(write = "?::status_type")
    private Status status;
}
