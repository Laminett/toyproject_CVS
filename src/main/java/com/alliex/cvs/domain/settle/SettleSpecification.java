package com.alliex.cvs.domain.settle;

import com.alliex.cvs.domain.user.User;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

public class SettleSpecification {

    public static Specification<Settle> withDate(String date) {
        return (root, query, builder) -> builder.equal(root.get("date"), date);
    }

    public static Specification<Settle> withUsername(final String username) {
        return new Specification<Settle>() {
            @Override
            public Predicate toPredicate(Root<Settle> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                Join<Settle, User> user = root.join("user");
                return criteriaBuilder.equal(user.get("username"), username);
            }
        };
    }

}