package com.example.calenduck.domain.performance.http;

import com.example.calenduck.domain.performance.repository.NameWithMt20idRepository;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BatchManagerTest {

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
        taskExecutor.setThreadNamePrefix("batch-test-");
        taskExecutor.initialize();

        batchManager = new BatchManager(httpRequest, dataConversion, nameWithMt20idRepository, taskExecutor);
    }

    @Test
    @DisplayName("주입된 ThreadPoolTaskExecutor로 병렬 처리가 정상 동작한다")
    void getElements_withValidMt20ids_returnsElements() throws Exception {
        // Given
        List<String> mt20ids = Arrays.asList("PF001", "PF002", "PF003");
        when(nameWithMt20idRepository.findAllMt20idsOrdered()).thenReturn(mt20ids);

        Elements elements1 = Jsoup.parse("<db><mt20id>PF001</mt20id></db>").select("db > *");
        Elements elements2 = Jsoup.parse("<db><mt20id>PF002</mt20id></db>").select("db > *");
        Elements elements3 = Jsoup.parse("<db><mt20id>PF003</mt20id></db>").select("db > *");

        when(httpRequest.requestExtraction("PF001")).thenReturn("<db><mt20id>PF001</mt20id></db>");
        when(httpRequest.requestExtraction("PF002")).thenReturn("<db><mt20id>PF002</mt20id></db>");
        when(httpRequest.requestExtraction("PF003")).thenReturn("<db><mt20id>PF003</mt20id></db>");
        when(dataConversion.convertXml("<db><mt20id>PF001</mt20id></db>")).thenReturn(elements1);
        when(dataConversion.convertXml("<db><mt20id>PF002</mt20id></db>")).thenReturn(elements2);
        when(dataConversion.convertXml("<db><mt20id>PF003</mt20id></db>")).thenReturn(elements3);

        // When
        List<Elements> result = batchManager.getElements();

        // Then
        assertThat(result).hasSize(3);
    }

    @Test
    @DisplayName("개별 API 호출 실패 시 해당 항목만 skip하고 나머지는 정상 처리한다")
    void getElements_withPartialFailure_skipsFailedItems() throws Exception {
        // Given
        List<String> mt20ids = Arrays.asList("PF001", "PF002", "PF003");
        when(nameWithMt20idRepository.findAllMt20idsOrdered()).thenReturn(mt20ids);

        Elements elements1 = Jsoup.parse("<db><mt20id>PF001</mt20id></db>").select("db > *");
        Elements elements3 = Jsoup.parse("<db><mt20id>PF003</mt20id></db>").select("db > *");

        when(httpRequest.requestExtraction("PF001")).thenReturn("<db><mt20id>PF001</mt20id></db>");
        when(httpRequest.requestExtraction("PF002")).thenThrow(new IOException("Connection refused"));
        when(httpRequest.requestExtraction("PF003")).thenReturn("<db><mt20id>PF003</mt20id></db>");
        when(dataConversion.convertXml("<db><mt20id>PF001</mt20id></db>")).thenReturn(elements1);
        when(dataConversion.convertXml("<db><mt20id>PF003</mt20id></db>")).thenReturn(elements3);

        // When
        List<Elements> result = batchManager.getElements();

        // Then
        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("빈 mt20id 리스트 입력 시 빈 결과를 반환한다")
    void getElements_withEmptyList_returnsEmptyResult() throws ExecutionException, InterruptedException {
        // Given
        when(nameWithMt20idRepository.findAllMt20idsOrdered()).thenReturn(Collections.emptyList());

        // When
        List<Elements> result = batchManager.getElements();

        // Then
        assertThat(result).isEmpty();
    }
}
