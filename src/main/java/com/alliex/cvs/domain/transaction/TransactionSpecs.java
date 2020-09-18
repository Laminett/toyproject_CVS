package com.alliex.cvs.domain.transaction;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class TransactionSpecs {

    public enum SearchKey {
        ID("id"),
        USERID("userId"),
        MERCHANTID("merchantId"),
        PAYMENTTYPE("paymentType"),
        POINT("point"),
        TRANSSTATE("state"),
        TRANSTYPE("type");

        private final String value;

        SearchKey(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static Specification<Transaction> searchWith(Map<SearchKey, Object> searchKeyword) {
        return (Specification<Transaction>) ((root, query, builder) -> {
            List<Predicate> predicate = getPredicateWithKeyword(searchKeyword, root, builder);

            return builder.and(predicate.toArray(new Predicate[0]));
        });
    }

    private static List<Predicate> getPredicateWithKeyword(Map<SearchKey, Object> searchKeyword, Root<Transaction> root, CriteriaBuilder builder) {
        List<Predicate> predicate = new ArrayList<>();
        for (SearchKey key : searchKeyword.keySet()) {
            switch (key) {
                case ID:
                case USERID:
                case MERCHANTID:
                case PAYMENTTYPE:
                case POINT:
                case TRANSSTATE:
                case TRANSTYPE:
                    predicate.add(builder.equal(
                            root.get(key.value), searchKeyword.get(key)
                    ));
                    break;
                default:
                    break;
            }
        }

        return predicate;
    }

}
