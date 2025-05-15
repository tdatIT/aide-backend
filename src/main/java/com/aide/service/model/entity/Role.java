package com.aide.service.model.entity;

import com.aide.service.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
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
    private Set<User> users = new HashSet<>();
} 