package com.example.calenduck.domain.performance.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RankingCountResponse {
    private final String name;
    private final long count;
}
