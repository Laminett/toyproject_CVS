package com.alliex.cvs.domain.settle;

import com.alliex.cvs.domain.user.User;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

public class SettleSpecification {

    public static Specification<Settle> withSearchData(String searchField, String searchData) {
        if ("aggregatedAt".equals(searchField)) {
            return (root, query, builder) -> builder.equal(root.get(searchField), searchData);
        } else if ("fullName".equals(searchField)) {
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

}