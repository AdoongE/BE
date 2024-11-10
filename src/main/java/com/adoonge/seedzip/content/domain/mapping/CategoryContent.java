package com.adoonge.seedzip.content.domain.mapping;

import com.adoonge.seedzip.category.domain.Category;
import com.adoonge.seedzip.content.domain.Contents;
import com.adoonge.seedzip.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryContent extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contentsId")
    private Contents contents;
}
