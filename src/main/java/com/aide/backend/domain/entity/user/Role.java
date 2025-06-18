package com.aide.backend.domain.entity.user;

import com.aide.backend.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roles")
public class Role extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_name", length = 50, nullable = false, unique = true)
    private String roleName;

    @Column(length = 500)
    private String description;

    @Column(name = "is_active", nullable = false)
    private boolean active;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;
}
