package com.adoonge.seedzip.simplification.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class SimplificationAllResponse {


    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class simplificationFileResponse{
        SimplificationInfoResponse simplificationInfo;
        List<String> files;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class simplificationLinkResponse{
        SimplificationInfoResponse simplificationInfo;
        String link;
    }

}
