package com.adoonge.seedzip.bookmark.domain;

import com.adoonge.seedzip.member.domain.Member;
import com.adoonge.seedzip.seed.domain.Seed;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Builder
@Entity
@Table(name = "seed_bookmarks")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SeedBookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seed_id")
    @OnDelete(action = OnDeleteAction.CASCADE) // Seed 삭제 시 Bookmark도 삭제
    private Seed seed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE) // Member 삭제 시 Bookmark도 삭제
    private Member member;
}
