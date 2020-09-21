package com.alliex.cvs.domain.point;

import com.alliex.cvs.domain.user.User;
import org.apache.commons.lang3.StringUtils;
import com.alliex.cvs.web.dto.PointHistoryRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class PointHistorySpecification {

    public static Specification<PointHistory> withSearchPeriod(String searchField, PointHistoryRequest pointHistoryRequest) {
        if (StringUtils.isNotBlank(pointHistoryRequest.getStartDate()) && StringUtils.isNotBlank(pointHistoryRequest.getEndDate())) {
            LocalDateTime startDate = LocalDateTime.of(LocalDate.parse(pointHistoryRequest.getStartDate(), DateTimeFormatter.ofPattern("yyyyMMdd")), LocalTime.of(0, 0, 0));
            LocalDateTime endDate = LocalDateTime.of(LocalDate.parse(pointHistoryRequest.getEndDate(), DateTimeFormatter.ofPattern("yyyyMMdd")), LocalTime.of(23, 59, 59));
            return ((root, query, criteriaBuilder) -> criteriaBuilder.between(root.get(searchField), startDate, endDate));
        } else {
            return null;
        }
    }

    public static Specification<PointHistory> withSearchData(String searchField, String searchData) {
        if ("fullName".equals(searchField)) {
            return new Specification<PointHistory>() {
                @Override
                public Predicate toPredicate(Root<PointHistory> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                    Join<PointHistory, User> user = root.join("user");
                    return criteriaBuilder.like(user.get(searchField), searchData + "%");
                }
            };
        } else if ("status".equals(searchField)) {
            return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(searchField), searchData));
        } else {
            throw new IllegalArgumentException("Wrong search field name: " + searchField);
        }
    }

}