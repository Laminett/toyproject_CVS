package com.alliex.cvs.domain.point;

import com.alliex.cvs.domain.user.User;
import com.alliex.cvs.web.dto.PointHistoryResponse;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PointHistorySpecification {

    public enum SearchKey {
        USERNAME("username"),
        STATUS("status");

        private final String value;

        SearchKey(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static Specification<PointHistory> searchWith(Map<SearchKey, Object> searchKeyword) {
        return (Specification<PointHistory>) ((root, query, builder) -> {
            List<Predicate> predicates = getPredicateWithKeyword(searchKeyword, root, builder);
            return builder.and(predicates.toArray(new Predicate[0]));
        });
    }

    private static List<Predicate> getPredicateWithKeyword(Map<SearchKey, Object> serchKeyword, Root<PointHistory> root, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();
        for(SearchKey key : serchKeyword.keySet()) {
            switch (key) {
                case STATUS:
                case USERNAME:
                    predicates.add(builder.equal(root.get(key.value),serchKeyword.get(key)));
                    break;
            }
        }
        return predicates;
    }
}
