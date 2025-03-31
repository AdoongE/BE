package com.adoonge.seedzip.seed.domain;

import com.adoonge.seedzip.global.entity.BaseEntity;
import com.adoonge.seedzip.member.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Builder
@Entity
@Table(name = "seeds")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Seed extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seed_id")
    private Long id;

    @Column(name = "seed_name", nullable = false)
    private String seedName;

    private LocalDate dDay;

    private String seedDetail;

    @Column(name = "seed_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private SeedType seedType;

    private Long thumbnailIdx;  // seedType = LINK, PDF인 경우 null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE) // Member 삭제 시 Content도 삭제
    private Member member;
}
