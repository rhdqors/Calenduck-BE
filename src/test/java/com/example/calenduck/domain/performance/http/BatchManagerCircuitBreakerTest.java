package com.example.calenduck.domain.performance.http;

import com.example.calenduck.domain.performance.repository.NameWithMt20idRepository;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BatchManagerCircuitBreakerTest {

    @Mock
    private HttpRequest httpRequest;

    @Mock
    private DataConversion dataConversion;

    @Mock
    private NameWithMt20idRepository nameWithMt20idRepository;

    private ThreadPoolTaskExecutor taskExecutor;
    private BatchManager batchManager;

    @BeforeEach
    void setUp() {
        taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(2);
        taskExecutor.setMaxPoolSize(4);
        taskExecutor.setQueueCapacity(10);
        taskExecutor.setThreadNamePrefix("cb-test-");
        taskExecutor.initialize();

        batchManager = new BatchManager(httpRequest, dataConversion, nameWithMt20idRepository, taskExecutor);
    }

    @Test
    @DisplayName("API 연속 실패 시 fallback이 빈 리스트를 반환한다")
    void getElementsFallback_returnsEmptyList() {
        // Given
        Exception testException = new ExecutionException("KOPIS API 장애", new RuntimeException());

        // When
        List<Elements> result = batchManager.getElementsFallback(testException);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("getElements에서 ExecutionException 발생 시 fallback 메서드가 빈 리스트를 반환한다")
    void getElementsFallback_onExecutionException_returnsEmptyList() {
        // Given
        ExecutionException exception = new ExecutionException("실행 에러", new RuntimeException("IO 에러"));

        // When
        List<Elements> result = batchManager.getElementsFallback(exception);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Repository가 빈 리스트를 반환하면 getElements도 빈 리스트를 반환한다")
    void getElements_withEmptyRepository_returnsEmptyList() throws Exception {
        // Given
        when(nameWithMt20idRepository.findAllMt20idsOrdered()).thenReturn(Collections.emptyList());

        // When
        List<Elements> result = batchManager.getElements();

        // Then
        assertThat(result).isEmpty();
    }
}
