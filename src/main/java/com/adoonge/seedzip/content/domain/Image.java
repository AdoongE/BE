package com.adoonge.seedzip.content.domain;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.adoonge.seedzip.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Setter
// @Entity
// @Table(name = "image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Image extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    @Column(nullable = false, length = 1000)
    private String imgLink;

    @Column(nullable = false)
    private String imgName;

    @Column(nullable = false)
    private boolean imgThumbnail = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contents_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Contents contents;
}
