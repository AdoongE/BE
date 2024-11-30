package com.adoonge.seedzip.summary.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageSummaryResponse {
    private String title;     // 제목
    private String summary;   // 요약 (3문장)
    private String tags;      // 태그 (쉼표로 구분된 문자열)

    // 기본 생성자 (필수)
    public ImageSummaryResponse() {}
}
