package com.example.calenduck.global;

import org.springframework.stereotype.Component;

@Component
public class DatabaseConfig {

    private String jdbcUrl = "jdbc:mysql://competition-admin.cjyqslqcsafp.ap-northeast-2.rds.amazonaws.com:3306/competition";
    private String username = "daecheol";
    private String password = "11223344";

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
