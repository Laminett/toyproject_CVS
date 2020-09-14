package com.alliex.cvs.domain.product;

import lombok.Getter;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ProductSpecs {

    public enum SearchKey {
        CATEGORY("categoryId"),
        NAME("name"),
        POINT("point");

        private final String value;

        SearchKey(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static Specification<Product> searchWith(Map<SearchKey, Object> searchKeyword) {
        return (Specification<Product>) ((root, query, builder) -> {
            List<Predicate> predicates = getPredicateWithKeyword(searchKeyword, root, builder);

            return builder.and(predicates.toArray(new Predicate[0]));
        });
    }

    private static List<Predicate> getPredicateWithKeyword(Map<SearchKey, Object> searchKeyword, Root<Product> root, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();
        for (SearchKey key : searchKeyword.keySet()) {
            switch (key) {
                case CATEGORY:
                case NAME:
                    predicates.add(builder.like(
                            root.get(key.value), "%" + searchKeyword.get(key) + "%"
                    ));
                case POINT:
                    predicates.add(builder.equal(
                            root.get(key.value), searchKeyword.get(key)
                    ));
                    break;
                default:
                    break;
            }
        }

        return predicates;
    }
}
