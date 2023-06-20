package com.example.calenduck.domain.performance.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.sql.*;
import java.util.*;

@Slf4j
@Component
public class XmlToMap {

    // Presto
    public ResultSet prestoQueryExample() throws SQLException {
        ResultSet resultSet = null;
        Connection connection = null;
        try {
            // JDBC 드라이버 로드
            Class.forName("com.facebook.presto.jdbc.PrestoDriver");
            Properties properties = new Properties();
            properties.setProperty("user", "test");

            // Presto에 연결
            connection = DriverManager.getConnection(
                    "jdbc:presto://3.38.112.170:8889/hive",
                    properties
            );

            // 쿼리 실행
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM  b_competition.name_with_mt20id";
            resultSet = statement.executeQuery(query);
            log.info("resultSet = " + resultSet);
            log.info("resultSet = " + resultSet.toString());

            // 이름, 공연id 테스트 출력
//            while (resultSet.next()) {
//                log.info(resultSet.getString(1)+", "+resultSet.getString(2));
//            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            if (resultSet != null) {
                resultSet.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return resultSet;
    }

    // Presto에서 조회한 값중 공연id만 리스트에 담기 (중복값은 하나만)
    public List<String> getPerformanceIdList() throws SQLException {
        List<String> performanceIds = new ArrayList<>();
        ResultSet resultSet = prestoQueryExample();
        try {
            while (resultSet.next()) {
                String performanceId = resultSet.getString(2);
                if (performanceId != null && !performanceId.trim().isEmpty() && !performanceIds.contains(performanceId)) {
                    performanceIds.add(performanceId);
                }
            }
            log.info("performanceIds = " + performanceIds.toString());
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
        }
        return performanceIds;
    }

//    public static void main(String[] args) throws SQLException {
//        getPerformanceIdList();
//    }

}
