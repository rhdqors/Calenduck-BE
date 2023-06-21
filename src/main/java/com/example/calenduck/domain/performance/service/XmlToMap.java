package com.example.calenduck.domain.performance.service;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.*;

@Slf4j
@Component
public class XmlToMap {

//    // Presto
//    public ResultSet prestoQueryExample() throws SQLException {
//        ResultSet resultSet = null;
//        Connection connection = null;
//        try {
//            // JDBC 드라이버 로드
//            Class.forName("com.facebook.presto.jdbc.PrestoDriver");
//            Properties properties = new Properties();
//            properties.setProperty("user", "test");
//
//            // Presto에 연결
//            connection = DriverManager.getConnection(
//                    "jdbc:presto://3.35.79.182:8889/hive",
//                    properties
//            );
//
//            // 쿼리 실행
//            Statement statement = connection.createStatement();
//            String query = "SELECT * FROM  b_competition.name_with_mt20id limit 100";
//            resultSet = statement.executeQuery(query);
//            log.info("resultSet = " + resultSet);
//            log.info("resultSet = " + resultSet.toString());
//
//            // 이름, 공연id 테스트 출력
////            while (resultSet.next()) {
////                log.info(resultSet.getString(1)+", "+resultSet.getString(2));
////            }
//
//        } catch (ClassNotFoundException | SQLException e) {
//            e.printStackTrace();
//            if (resultSet != null) {
//                resultSet.close();
//            }
//            if (connection != null) {
//                connection.close();
//            }
//        }
//        return resultSet;
//    }


//    // 1. presto 연결
//    public static Statement ConnectionPresto() throws SQLException {
//        Connection connection = null;
//        try {
//            Class.forName("com.facebook.presto.jdbc.PrestoDriver");
//            Properties properties = new Properties();
//            properties.setProperty("user", "test");
//
//            // Presto에 연결
//            connection = DriverManager.getConnection(
//                    "jdbc:presto://3.35.79.182:8889/hive",
//                    properties
//            );
//
//        } catch (ClassNotFoundException | SQLException e) {
//            e.printStackTrace();
//            if (connection != null) {
//                connection.close();
//            }
//        }
//        return connection.createStatement();
//    }
//
//    // 2. presto에서 mt20id 쿼리실행 및 상세정보 xml파일 불러와 resultSet에 넣기
//    public static ResultSet getMt20idResultSet() throws SQLException {
//        Statement statement = ConnectionPresto();
//        ResultSet resultSet = null;
//        try {
//            String query = "SELECT * FROM  b_competition.name_with_mt20id limit 100";
//            resultSet = statement.executeQuery(query);
//            log.info("resultSet = " + resultSet);
//            log.info("resultSet = " + resultSet.toString());
//
//            // 이름, 공연id 테스트 출력
////        while (resultSet.next()) {
////            log.info(resultSet.getString(1)+", "+resultSet.getString(2));
////        }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            if (resultSet != null) {
//                resultSet.close();
//            }
//        }
//        return resultSet;
//    }

    // 1. RDS 연결
    public Connection connectToRDS() throws SQLException {
        Connection connection = null;
        try {
            String jdbcUrl = "jdbc:mysql://competition.cjyqslqcsafp.ap-northeast-2.rds.amazonaws.com:3306/competition";
            String username = "competition";
            String password = "!g794613";

            connection = DriverManager.getConnection(jdbcUrl, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            if (connection != null) {
                connection.close();
            }
        }
        return connection;
    }

    // 2. presto mt20id 쿼리 실행 및 상세정보 xml 파일 가져오기
//    public static ResultSet getMt20idResultSet() throws SQLException {
//        Connection connection = connectToRDS();
//        Statement statement = null;
//        ResultSet resultSet = null;
//
//        try {
//            statement = connection.createStatement();
//            String query = "SELECT * FROM competition.name_with_mt20id LIMIT 100";
//            resultSet = statement.executeQuery(query);
//
//            // 이름, 공연id 테스트 출력
//            while (resultSet.next()) {
//                String name = resultSet.getString(1);
//                String performanceId = resultSet.getString(2);
//                log.info(name + ", " + performanceId);
//                log.info("--------resultset123 = " + resultSet.toString());
//            }
//
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            if (resultSet != null) {
//                resultSet.close();
//            }
//        }
//        log.info("--------resultset = " + resultSet.toString());
////        log.info("--------resultset1 = " + resultSet.getString(2));
//        return resultSet;
//    }


    // 2 + 3
    public List<String> getMt20idResultSet() throws SQLException {
        Connection connection = connectToRDS();
        List<String> performanceIds = new ArrayList<>();
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.createStatement();
//            String query = "SELECT * FROM competition.name_with_mt20id LIMIT 100";
            String query = "SELECT * FROM competition.name_with_mt20id ORDER BY mt20id ASC LIMIT 100";
            resultSet = statement.executeQuery(query);

            // 이름, 공연id 테스트 출력
            while (resultSet.next()) {
                String name = resultSet.getString(1);
                String performanceId = resultSet.getString(2);
                performanceIds.add(performanceId);
                log.info(name + ", " + performanceId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            if (resultSet != null) {
                resultSet.close();
            }
        }
        return performanceIds;
    }

    // 3. Presto에서 조회한 값중 공연id만 리스트에 담기 (중복값은 하나만)
//    public static List<String> getPerformanceIdList() throws SQLException {
//        List<String> performanceIds = new ArrayList<>();
//        ResultSet resultSet = getMt20idResultSet();
//
//        try {
//            while (resultSet.next()) {
//                String performanceId = resultSet.getString(2);
//                log.info("--------performanceId = " + performanceId);
//                if (performanceId != null && !performanceId.trim().isEmpty() && !performanceIds.contains(performanceId)) {
//                    performanceIds.add(performanceId);
//                }
//            }
//            log.info("performanceIds = " + performanceIds.toString());
//        } finally {
//            // Move the resultSet.close() statement here
//            if (resultSet != null) {
//                resultSet.close();
//            }
//        }
//        return performanceIds;
//    }

    // 4. xml -> 타입 변환
    // poster = elements.select("poster").text(); 등으로 컬럼 지정 가능
    public List<Elements> getElements() throws SQLException, IOException {
//        List<String> performanceIds = getPerformanceIdList();
        List<String> performanceIds = getMt20idResultSet();
        List<Elements> elements = new ArrayList<>();

        // --- url + id 조합으로 공연별 상세정보 조회 ---
        for (String performanceId : performanceIds) {
            log.info("performanceId = " + performanceId);

            StringBuilder response = new StringBuilder();
            URL url = new URL("http://kopis.or.kr/openApi/restful/pblprfr/" + performanceId + "?service=60a3d3573c5e4d8bb052a4abebff27b6");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            log.info("Response Code: " + responseCode);

            // Read the response
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // response = 공연id의 상세정보 xml 보기
//            log.info("Response XML:\n" + response.toString());

            // --- xml -> 문서 -> 문자열변수 저장 ---
            Document doc = Jsoup.parse(response.toString());
//            log.info("doc = " + doc.toString());

            // 데이터 <db> 요소 안의 모든 데이터를 선택
            elements.add(doc.select("db > *"));

            log.info("elements = " + elements.toString());
        }

        return elements;
    }

//    public static void main(String [] args) throws SQLException {
//        getPerformanceIdList();
//    }

}