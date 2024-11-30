package com.adoonge.seedzip.content.dto.response;

import com.adoonge.seedzip.content.domain.Contents;
import com.adoonge.seedzip.content.domain.ContentsDataType;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


public class ContentsAllResponse {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class contentsInfo{
        Long contentId;
        String contentName;
        List<Long> categoryId;
        List<String> categoryName;
        ContentsDataType contentDateType;
        String thumbnailImage; // 없으면 null
        LocalDateTime updatedDt;
        List<Long> tagId;
        List<String> tagName;
        int dDay;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class getAllContents{
        String nickname;
        List<contentsInfo> contentsInfoList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class getContents{
        Long contentId;
        ContentsDataType contentDataType;
        String contentName;
        String contentLink;
        List<String> contentImage;
        List<String> contentDoc;
        List<String> title;
        Long thumbnailImage;
        List<String> boardCategory;
        List<String> tags;
        LocalDate dDay;
        String contentDetail;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class contentResponse{
        private String msg;
        private Long contentId;
        private Long memberId;

        public static contentResponse fromEntity(String msg, Contents contents) {
            return contentResponse.builder()
                    .msg(msg)
                    .contentId(contents.getContentsId())
                    .memberId(contents.getMember().getId())
                    .build();
        }
    }
}
