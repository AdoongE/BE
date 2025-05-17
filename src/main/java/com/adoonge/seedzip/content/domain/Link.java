package com.adoonge.seedzip.content.domain;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.adoonge.seedzip.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
// @Entity
// @Table(name = "link")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Link extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long linkId;

    @Column(nullable = false, length = 1000)
    private String link;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contents_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Contents contents;
}
