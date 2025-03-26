package com.adoonge.seedzip.seed.domain.mapping;

import com.adoonge.seedzip.category.domain.Category;
import com.adoonge.seedzip.global.entity.BaseEntity;
import com.adoonge.seedzip.seed.domain.Seed;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategorySeed extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @OnDelete(action = OnDeleteAction.CASCADE)  //Category 삭제시 CategoryContent 삭제됨
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seed_id")
    @OnDelete(action = OnDeleteAction.CASCADE)  //Seed 삭제시 CategorySeed 삭제됨
    private Seed seed;
}
