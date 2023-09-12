package com.example.calenduck.domain.performance.service;

import com.example.calenduck.domain.bookmark.Entity.Bookmark;
import com.example.calenduck.domain.bookmark.Service.BookmarkService;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.api.mockito.PowerMockito;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class XmlToMapTest {

    @InjectMocks
    private XmlToMap xmlToMap;

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<String> typedQuery;

    @Mock
    private ExecutorService executorService;

    @Mock
    private HttpURLConnection httpConnection;

    @Mock
    private BookmarkService bookmarkService;



    @Nested
    @DisplayName("성공 케이스")
    class SuccessCase {
        @Test
        @DisplayName("db에 저장된 Mt20id 배열에 저장 -> http요청 위해")
        void getMt20idResultSetTest() {
            // given
            List<String> mockResultList = Arrays.asList("ID1", "ID2", "ID2", "ID3");
            given(entityManager.createQuery(anyString(), eq(String.class))).willReturn(typedQuery);
            given(typedQuery.getResultList()).willReturn(mockResultList);
            // when
            List<String> result = xmlToMap.getMt20idResultSet();
            // then
            assertEquals(3, result.size());
            assertTrue(result.contains("ID1"));
            assertTrue(result.contains("ID2"));
            assertTrue(result.contains("ID3"));
        }

//        @Test
//        @DisplayName("http요청(배치, 스레드풀) 및 문서로 가져오기")
//        void getElements() {
//            List<String> performanceIds = Arrays.asList("ID1", "ID2", "ID2", "ID3");
//            List<Elements> elementsList = new ArrayList<>();
//
//            //given
//            given();
//
//            //when
//            List<String> result = xmlToMap.getMt20idResultSet();
//
//            //then
//
//        }

        @Test
        @DisplayName("performanceId 배치 나눔")
        void createBatches() {
            // given
            List<String> performanceIds = Arrays.asList("ID1", "ID2", "ID3", "ID4");
            int batchSize = 2;
            // when
            List<List<String>> result = xmlToMap.createBatches(performanceIds, batchSize);
            // then
            assertEquals(2, result.size());
            assertEquals(List.of("ID1", "ID2"), result.get(0));
            assertEquals(List.of("ID3", "ID4"), result.get(1));
        }

//        @Test
//        @DisplayName("배치 비동기 처리 - 멀티스레드")
//        void processBatchesAsync() {
//            // given
//            int batchSize = 2;
//            List<String> performanceIds = Arrays.asList("ID1", "ID2", "ID3", "ID4");
//            List<List<String>> batches = xmlToMap.createBatches(performanceIds, batchSize);
//            // when
//            List<CompletableFuture<List<Elements>>> futures = xmlToMap.processBatchesAsync(batches, executorService);
//            // then
//            List<List<Elements>> results = futures.stream()
//                    .map(CompletableFuture::join)
//                    .collect(Collectors.toList());
//
//            assertNotNull(results);
//            assertEquals(2, results.size());
//        }

//        @Test
//        @DisplayName("각 배치 처리")
//        public void processBatch() throws Exception {
//            // Given
//            List<String> batch = Arrays.asList("test1", "test2");
//
//            String responseXml = "<db><item1>value1</item1><item2>value2</item2></db>";
//
//            InputStream stream = new ByteArrayInputStream(responseXml.getBytes());
//
//            httpConnection = Mockito.mock(HttpURLConnection.class);
//            given(httpConnection.getInputStream()).willReturn(stream);
//            given(httpConnection.getResponseCode()).willReturn(200);
//
//            URL url = Mockito.mock(URL.class);
//            given(url.openConnection()).willReturn(httpConnection);
//
//            // 모킹된 URL 객체를 실제 코드에서 사용할 수 있도록 설정
//            PowerMockito.whenNew(URL.class).withAnyArguments().thenReturn(url);
//
//            // When
//            List<Elements> batchElements = xmlToMap.processBatch(batch);
//
//            // Then
//            assertEquals(2, batchElements.size());
//
//            // Log the document content to verify the parsing
//            System.out.println(batchElements.get(0).toString());
//
//            assertEquals("value1", batchElements.get(0).select("item1").text());
//            assertEquals("value2", batchElements.get(0).select("item2").text());
//        }

        @Test
        @DisplayName("모든 CompletableFuture 작업이 완료될 때까지 대기")
        void waitForCompletion() {
            // given
            CompletableFuture<List<Elements>> future1 = CompletableFuture.completedFuture(new ArrayList<>());
            CompletableFuture<List<Elements>> future2 = CompletableFuture.completedFuture(new ArrayList<>());
            List<CompletableFuture<List<Elements>>> futures = Arrays.asList(future1, future2);
            // when
            xmlToMap.waitForCompletion(futures);
            // then
            assertTrue(future1.isDone());
            assertTrue(future2.isDone());
        }

        @Test
        @DisplayName("각 CompletableFuture의 결과를 최종 목록에 추가")
        void retrieveResults() throws ExecutionException, InterruptedException {
            // given
            Elements elements1 = Mockito.mock(Elements.class);
            Elements elements2 = Mockito.mock(Elements.class);

            CompletableFuture<List<Elements>> future1 = CompletableFuture.completedFuture(Arrays.asList(elements1));
            CompletableFuture<List<Elements>> future2 = CompletableFuture.completedFuture(Arrays.asList(elements2));

            List<CompletableFuture<List<Elements>>> futures = Arrays.asList(future1, future2);
            List<Elements> elementsList = new ArrayList<>();

            // when
            xmlToMap.retrieveResults(futures, elementsList);

            // then
            assertEquals(2, elementsList.size());
            assertTrue(elementsList.contains(elements1));
            assertTrue(elementsList.contains(elements2));


        }

//        @Test
//        @DisplayName("Test getBookmarkElements method")
//        void getBookmarkElements() throws Exception {
//            // given
//            String mt20id = "mt20id";
//
//            Bookmark bookmark1 = new Bookmark();
//            bookmark1.setMt20id("id1");
//            Bookmark bookmark2 = new Bookmark();
//            bookmark2.setMt20id("id2");
//
//            List<Bookmark> bookmarks = Arrays.asList(bookmark1, bookmark2);
//            given(bookmarkService.findBookmarks(mt20id)).willReturn(bookmarks);
//
//            URL url = Mockito.mock(URL.class);
//            given(url.openConnection()).willReturn(httpConnection);
//
//            String responseXml = "<db><item1>value1</item1><item2>value2</item2></db>";
//            InputStream stream = new ByteArrayInputStream(responseXml.getBytes());
//            given(httpConnection.getInputStream()).willReturn(stream);
//
//            PowerMockito.whenNew(URL.class).withAnyArguments().thenReturn(url);
//
//            // when
//            List<Elements> elementsList = xmlToMap.getBookmarkElements(mt20id);
//
//            // then
//            assertEquals(2, elementsList.size());
//
//            Elements elements = elementsList.get(0);
//            assertEquals("value1", elements.select("item1").text());
//            assertEquals("value2", elements.select("item2").text());
//        }




    }// 성공

}// Class