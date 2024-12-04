package com.adoonge.seedzip.tag.domain;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.adoonge.seedzip.global.entity.BaseEntity;
import com.adoonge.seedzip.member.domain.Member;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tags")
public class Tag extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long id;

    @Column(name = "tag_name", unique = true)
    private String tagName;

    @Column(name = "is_default")
    private boolean isDefault = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @Builder
    public Tag(String tagName, boolean isDefault, Member member) {
        this.tagName = tagName;
        this.isDefault = isDefault;
        this.member = member;
    }

}
