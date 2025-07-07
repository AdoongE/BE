package com.adoonge.seedzip.faq.domain;

import com.adoonge.seedzip.faq.dto.request.FaqUpdateRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "faqs")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Faq {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FaqType type;

    @Column(nullable = false)
    private String question;

    @Lob
    @Column(nullable = false)
    private String answer;

    @Column(nullable = false, columnDefinition = "TEXT")
    private Integer orderIndex;  // 정렬 순서 (Q1, Q2 등)

    public void update(FaqUpdateRequest request) {
        if (request.question() != null) this.question = request.question();
        if (request.answer() != null) this.answer = request.answer();
        if (request.type() != null) this.type = request.type();
        if (request.orderIndex() != null) this.orderIndex = request.orderIndex();
    }

}
