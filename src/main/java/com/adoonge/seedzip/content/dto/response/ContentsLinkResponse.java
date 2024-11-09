package com.adoonge.seedzip.content.dto.response;

import com.adoonge.seedzip.content.domain.Contents;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ContentsLinkResponse {
    private String msg;
    private Long contentId;
    private Long memberId;

    public static ContentsLinkResponse fromEntity(String msg, Contents contents) {
        return ContentsLinkResponse.builder()
                .msg(msg)
                .contentId(contents.getContentsId())
                .memberId(contents.getMember().getId())
                .build();
    }
}
