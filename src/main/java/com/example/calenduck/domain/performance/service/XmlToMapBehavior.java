package com.example.calenduck.domain.performance.service;

import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface XmlToMapBehavior {

    List<String> queryForSaveMt20id(); // db에서 mt20id 꺼내어 저장
    List<Elements> getElements() throws InterruptedException, ExecutionException; // 저장한 mt20id로  http 요청하여 elements로 반환
    List<Elements> getBookmarkElements(String mt20id) throws IOException; // 찜목록 mt20id 상세정보 조회
}
