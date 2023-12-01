package com.example.calenduck.domain.performance.http;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataConversion {

    public Elements convertXml(String xmlResponse) {
        Document doc = Jsoup.parse(xmlResponse);
        return doc.select("db > *");
    }

}
