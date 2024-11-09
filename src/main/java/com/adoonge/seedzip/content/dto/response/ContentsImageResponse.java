package com.adoonge.seedzip.content.dto.response;

import com.adoonge.seedzip.content.domain.Contents;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ContentsImageResponse {
    private String msg;
    private Long contentId;
    private Long memberId;

    public static ContentsImageResponse fromEntity(String msg, Contents contents) {
        return ContentsImageResponse.builder()
                .msg(msg)
                .contentId(contents.getContentsId())
                .memberId(contents.getMember().getId())
                .build();
    }
}
