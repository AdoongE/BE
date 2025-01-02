package com.adoonge.seedzip.content.dto.request;

import java.util.ArrayList;

public record ContentsUrlRequest(
        ArrayList<String> fileUrls,
        ArrayList<String> fileNames
) {
}
