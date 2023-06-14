package com.example.calenduck.domain.user.entity;

public enum UserRoleEnum {
    USER(Authority.USER);  // 유저 권한

    private final String authority;

    UserRoleEnum(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }

    public static class Authority {
        public static final String USER = "ROLE_USER";
    }

}
