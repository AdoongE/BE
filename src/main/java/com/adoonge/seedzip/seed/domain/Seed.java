package com.adoonge.seedzip.seed.domain;

import com.adoonge.seedzip.global.entity.BaseEntity;
import com.adoonge.seedzip.member.domain.Member;
import com.adoonge.seedzip.seed.domain.mapping.CategorySeed;
import com.adoonge.seedzip.seed.domain.mapping.SeedTag;

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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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

    @Column(name = "view_count", nullable = false)
    private long viewCount = 0L; // 조회수

    @Version
    private Long version;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE) // Member 삭제 시 Content도 삭제
    private Member member;

    @OneToMany(mappedBy = "seed", fetch = FetchType.LAZY)
    private Set<File> files = new HashSet<>();

    @OneToMany(mappedBy = "seed", fetch = FetchType.LAZY)
    private Set<SeedTag> seedTags = new HashSet<>();

    @OneToMany(mappedBy = "seed", fetch = FetchType.LAZY)
    private Set<CategorySeed> categorySeeds = new HashSet<>();



    public void updateSeedName(String seedName) {
        this.seedName = seedName;
    }

    public void updateDDay(LocalDate dDay) {
        this.dDay = dDay;
    }

    public void updateSeedDetail(String seedDetail) {
        this.seedDetail = seedDetail;
    }

    public void updateThumbnailIdx(Long thumbnailIdx) {
        this.thumbnailIdx = thumbnailIdx;
    }

    public void incrementViewCount() {
        this.viewCount++;
    }


}
