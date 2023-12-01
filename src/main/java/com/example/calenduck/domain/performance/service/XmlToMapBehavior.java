package com.example.calenduck.domain.performance.service;

import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

public interface XmlToMapBehavior {
    List<Elements> getBookmarkElements(String mt20id) throws IOException; // 찜목록 mt20id 상세정보 조회
}
