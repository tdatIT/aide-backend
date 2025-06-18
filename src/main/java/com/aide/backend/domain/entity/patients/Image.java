package com.aide.backend.domain.entity.patients;

import com.aide.backend.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "images")
public class Image extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "public_id", nullable = false)
    private String publicId;

    @Column(name = "url", nullable = false, length = 1000)
    private String url;

    @Column(name = "format")
    private String format;

    @Column(name = "size")
    private Integer size;

    @Column(name = "width")
    private Integer width;

    @Column(name = "height")
    private Integer height;
}
