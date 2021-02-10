package com.alliex.cvs.domain.point;

import com.alliex.cvs.entity.PointHistory;
import com.alliex.cvs.entity.User;
import com.alliex.cvs.web.dto.PointHistoryRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.time.LocalDateTime;

public class PointHistorySpecification {

    public static Specification<PointHistory> withSearchPeriod(String searchField, PointHistoryRequest pointHistoryRequest) {
        if (pointHistoryRequest.getStartDate() != null && pointHistoryRequest.getEndDate() != null) {
            LocalDateTime startDate = pointHistoryRequest.getStartDate().atStartOfDay();
            LocalDateTime endDate = pointHistoryRequest.getEndDate().atTime(23, 59, 59);
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