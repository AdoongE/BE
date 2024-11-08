package com.adoonge.seedzip.content.domain;

import com.adoonge.seedzip.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Entity
@Table(name = "link")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Link extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long linkId;

    @Column(nullable = false)
    private String link;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contents_id")
    private Contents contents;
}
