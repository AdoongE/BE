package com.adoonge.seedzip.recommendation.dto.response;

import com.adoonge.seedzip.recommendation.dto.TextMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Choice {
    private int index;
    private TextMessage message;
}
