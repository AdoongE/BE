package com.adoonge.seedzip.content.domain.mapping;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.adoonge.seedzip.content.domain.Contents;
import com.adoonge.seedzip.content.domain.Tag;
import com.adoonge.seedzip.global.entity.BaseEntity;
import jakarta.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentTag extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contents_id")
    @OnDelete(action = OnDeleteAction.CASCADE)  //Content 삭제시 ContentTag 삭제됨
    private Contents contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tagId")
    private Tag tag;
}
