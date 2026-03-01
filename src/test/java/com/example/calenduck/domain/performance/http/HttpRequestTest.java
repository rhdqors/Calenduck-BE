package com.example.calenduck.domain.performance.http;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HttpRequestTest {

    private MockWebServer mockWebServer;
    private HttpRequest httpRequest;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        httpRequest = new HttpRequest();

        String baseUrl = mockWebServer.url("/openApi/restful/pblprfr/").toString();
        ReflectionTestUtils.setField(httpRequest, "baseUrl", baseUrl);
        ReflectionTestUtils.setField(httpRequest, "serviceKey", "testKey");
        ReflectionTestUtils.setField(httpRequest, "connectTimeout", 1000);
        ReflectionTestUtils.setField(httpRequest, "readTimeout", 1000);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    @DisplayName("정상 응답 시 XML 문자열을 반환한다")
    void requestExtraction_withValidResponse_returnsXml() throws IOException {
        // Given
        String xmlBody = "<db><mt20id>PF001</mt20id><prfnm>테스트공연</prfnm></db>";
        mockWebServer.enqueue(new MockResponse()
                .setBody(xmlBody)
                .setResponseCode(200));

        // When
        String result = httpRequest.requestExtraction("PF001");

        // Then
        assertThat(result).contains("<mt20id>PF001</mt20id>");
    }

    @Test
    @DisplayName("읽기 타임아웃 초과 시 IOException이 발생한다")
    void requestExtraction_withReadTimeout_throwsIOException() {
        // Given
        mockWebServer.enqueue(new MockResponse()
                .setBody("<db></db>")
                .setHeadersDelay(3, TimeUnit.SECONDS));

        // When & Then
        assertThatThrownBy(() -> httpRequest.requestExtraction("PF001"))
                .isInstanceOf(IOException.class);
    }
}
