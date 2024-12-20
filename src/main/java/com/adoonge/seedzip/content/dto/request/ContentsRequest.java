package com.adoonge.seedzip.content.dto.request;

import com.adoonge.seedzip.content.domain.Contents;
import com.adoonge.seedzip.content.domain.ContentsDataType;
import com.adoonge.seedzip.content.domain.Document;
import com.adoonge.seedzip.content.domain.Image;
import com.adoonge.seedzip.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


public class ContentsRequest {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class allContentsRequest {
        private ContentsDataType dataType;
        private String contentName; // 없으면 null
        private String[] boardCategory;
        private int thumbnailImage;
        private String contentLink;
        private String[] tags;
        private LocalDate dDay; // 없으면 null
        private String contentDetail; // 없으면 null

        public Contents toContentEntity(Member member){
            return Contents.builder()
                    .contentsName(contentName)
                    .dDay(dDay)
                    .contentsDetail(contentDetail)
                    .thumbnailIdx(thumbnailImage)
                    .contentsDataType(dataType)
                    .member(member)
                    .build();
        }

        public Document toDocEntity(Contents contents, String docLink, String docName){
            return Document.builder()
                    .docLink(docLink)
                    .contents(contents)
                    .docName(docName)
                    .build();
        }

        public Image toImgEntity(Contents contents, String imgLink, String imgName){
            return Image.builder()
                    .imgLink(imgLink)
                    .contents(contents)
                    .imgName(imgName)
                    .build();
        }
    }
}
