package com.alliex.cvs.domain.type;

import com.alliex.cvs.web.dto.UserRequest;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum UserSearchType {

    USERNAME("username"),
    FULL_NAME("fullName"),
    EMAIL("email"),
    DEPARTMENT("department");

    private final String field;

    UserSearchType(String field) {
        this.field = field;
    }

    public static Map<UserSearchType, String> getPredicateData(UserRequest userRequest) {
        Map<UserSearchType, String> predicateData = new HashMap<>();

        if (StringUtils.isNotBlank(userRequest.getUsername())) {
            predicateData.put(USERNAME, userRequest.getUsername());
        }

        if (StringUtils.isNotBlank(userRequest.getFullName())) {
            predicateData.put(FULL_NAME, userRequest.getFullName());
        }

        if (StringUtils.isNotBlank(userRequest.getEmail())) {
            predicateData.put(EMAIL, userRequest.getEmail());
        }

        if (StringUtils.isNotBlank(userRequest.getDepartment())) {
            predicateData.put(DEPARTMENT, userRequest.getDepartment());
        }

        return predicateData;
    }

}


