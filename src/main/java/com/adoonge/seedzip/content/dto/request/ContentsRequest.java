package com.adoonge.seedzip.content.dto.request;

import com.adoonge.seedzip.content.domain.Contents;
import com.adoonge.seedzip.content.domain.ContentsDataType;
import com.adoonge.seedzip.content.domain.Document;
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
    public static class docContentsRequest {
        private ContentsDataType dataType;
        private String contentName; // 없으면 null
        private String[] boardCategory;
        private String[] tags;
        private LocalDate dDay; // 없으면 null
        private String contentDetail; // 없으면 null

        public Contents toContentEntity(Member member){
            return Contents.builder()
                    .contentsName(contentName)
                    .dDay(dDay)
                    .contentsDetail(contentDetail)
                    .contentsDataType(dataType)
                    .member(member)
                    .build();
        }

        public Document toDocEntity(Contents contents, String docLink){
            return Document.builder()
                    .docLink(docLink)
                    .contents(contents)
                    .build();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class linkContentsRequest {
        private ContentsDataType dataType;
        private String contentName; // 없으면 null
        private String[] boardCategory;
        private String[] tags;
        private LocalDate dDay; // 없으면 null
        private String contentDetail; // 없으면 null

        public Contents toContentEntity(Member member){
            return Contents.builder()
                    .contentsName(contentName)
                    .dDay(dDay)
                    .contentsDetail(contentDetail)
                    .contentsDataType(dataType)
                    .member(member)
                    .build();
        }

        public Document toDocEntity(Contents contents, String docLink){
            return Document.builder()
                    .docLink(docLink)
                    .contents(contents)
                    .build();
        }
    }
}
