package com.adoonge.seedzip.content.dto.response;

import com.adoonge.seedzip.content.domain.Contents;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ContentsDocResponse {

    private String msg;
    private Long contentId;
    private Long memberId;

    public static ContentsDocResponse fromEntity(String msg, Contents contents) {
        return ContentsDocResponse.builder()
                .msg(msg)
                .contentId(contents.getContentsId())
                .memberId(contents.getMember().getId())
                .build();
    }
}
