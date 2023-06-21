package com.example.calenduck.global;

import org.springframework.stereotype.Component;

@Component
public class DatabaseConfig {

    private String jdbcUrl = "jdbc:mysql://competition.cjyqslqcsafp.ap-northeast-2.rds.amazonaws.com:3306/competition";
    private String username = "competition";
    private String password = "!g794613";

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
