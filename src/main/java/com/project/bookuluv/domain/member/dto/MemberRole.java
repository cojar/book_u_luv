package com.project.bookuluv.domain.member.dto;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum MemberRole {
    SUPERADMIN("ROLE_SUPERADMIN"),
    ADMIN("ROLE_ADMIN"),
    AUTHOR("ROLE_AUTHOR"),
    MEMBER("ROLE_MEMBER"),
    USER("ROLE_USER"); // 변경된 부분

    MemberRole(String value) {
        this.value = value;
    }

    private final String value;

    public String getValue() {
        return value;
    }

    public String getAuthority() {
        return "ROLE_" + name();
    }

    public static List<MemberRole> getAllRoles() {
        return Arrays.asList(MemberRole.values());
    }

    public static List<String> getAllRolesAsString() {
        return getAllRoles().stream()
                .map(MemberRole::getValue)
                .collect(Collectors.toList());
    }
}




