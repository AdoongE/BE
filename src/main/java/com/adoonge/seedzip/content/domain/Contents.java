package com.adoonge.seedzip.content.domain;

import com.adoonge.seedzip.global.entity.BaseEntity;
import com.adoonge.seedzip.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Builder
// @Entity
// @Table(name = "content")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Contents extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contentsId;

    private String contentsName;

    private LocalDate dDay;

    private String contentsDetail;

    @Enumerated(EnumType.STRING)
    private ContentsDataType contentsDataType;

    private int thumbnailIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE) // Member 삭제 시 Content도 삭제
    private Member member;
}
