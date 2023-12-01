package com.example.calenduck.domain.performance.service;

import com.example.calenduck.domain.performance.dto.response.SearchRankResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PerformanceSearchService implements PerformanceSearchBehavior {
    private final RedisTemplate<String, String> redisTemplate;
    //    Redis에 데이터 저장 및 업데이트.
    //    Redis에서 데이터 검색 및 가져오기.
    //    Redis의 리스트, 집합, 해시 등과 같은 다양한 데이터 구조 사용.
    //    Redis 트랜잭션 관리.
    //    Pub/Sub 메시징 시스템을 사용하여 메시지 발행 및 구독.

    @Override
    @Transactional
    public void updatePopularSearchWord(String searchTerm) {
        redisTemplate.opsForZSet().incrementScore("rank", searchTerm, 1);
    }

    @Override
    @Transactional
    public List<SearchRankResponseDto> searchRankList() {
        String key = "rank";
        // ZSetOperations 객체 생성
        ZSetOperations<String, String> ZSetOperations = redisTemplate.opsForZSet();
        // score순으로 5개 보여줌
        Set<ZSetOperations.TypedTuple<String>> typedTuples = ZSetOperations.reverseRangeWithScores(key, 0, 4);
        return typedTuples.stream().map(SearchRankResponseDto::convertToResponseRankingDto).collect(Collectors.toList());
    }

}
