package com.adoonge.seedzip.content.domain.mapping;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.adoonge.seedzip.category.domain.Category;
import com.adoonge.seedzip.content.domain.Contents;
import com.adoonge.seedzip.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

// @Entity
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
    @OnDelete(action = OnDeleteAction.CASCADE)  //Category 삭제시 CategoryContent 삭제됨
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contents_id")
    @OnDelete(action = OnDeleteAction.CASCADE)  //Content 삭제시 CategoryContent 삭제됨
    private Contents contents;
}
