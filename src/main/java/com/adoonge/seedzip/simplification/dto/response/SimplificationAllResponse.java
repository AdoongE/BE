package com.adoonge.seedzip.simplification.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class simplificationLinkResponse{
        SimplificationInfoResponse simplificationInfo;
        String link;
    }

}
