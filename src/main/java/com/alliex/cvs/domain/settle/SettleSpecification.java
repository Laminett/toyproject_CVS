package com.alliex.cvs.domain.settle;

import com.alliex.cvs.domain.user.User;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.time.LocalDate;

public class SettleSpecification {

    public static Specification<Settle> withSearchData(String searchField, String searchData) {
        if ("fullName".equals(searchField)) {
            return new Specification<Settle>() {
                @Override
                public Predicate toPredicate(Root<Settle> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                    Join<Settle, User> user = root.join("user");
                    return criteriaBuilder.like(user.get(searchField), searchData + "%");
                }
            };
        } else {
            throw new IllegalArgumentException("Wrong search field name: " + searchField);
        }
    }

    public static Specification<Settle> withSearchDate(LocalDate aggregatedAt) {
        if (aggregatedAt == null) {
            return null;
        } else {
            return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("aggregatedAt"), aggregatedAt));
        }
    }

}