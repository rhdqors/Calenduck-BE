package com.example.calenduck.domain.performance.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.ZSetOperations;

@Getter
@Setter
public class SearchRankResponseDto {
    private String rankKeyword;
    private Integer rank;

    public static SearchRankResponseDto convertToResponseRankingDto(ZSetOperations.TypedTuple<String> stringTypedTuple) {
        SearchRankResponseDto searchRankResponseDto = new SearchRankResponseDto();
        searchRankResponseDto.setRankKeyword(stringTypedTuple.getValue());
        searchRankResponseDto.setRank(stringTypedTuple.getScore().intValue());
        return searchRankResponseDto;
    }
}
