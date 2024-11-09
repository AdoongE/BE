package com.adoonge.seedzip.content.domain;

import com.adoonge.seedzip.global.entity.BaseEntity;
import com.adoonge.seedzip.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@Entity
@Table(name = "content")
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
