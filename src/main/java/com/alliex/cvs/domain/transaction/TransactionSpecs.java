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
        BUYERID("buyerId"),
        MERCHANTID("merchantId"),
        PAYMENTTYPE("paymentType"),
        TRANSPOINT("transPoint"),
        TRANSSTATE("transState"),
        TRANSTYPE("transType");

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
                case BUYERID:
                case MERCHANTID:
                case PAYMENTTYPE:
                case TRANSPOINT:
                case TRANSSTATE:
                case TRANSTYPE:
                    predicate.add(builder.like(
                            root.get(key.value), "%" + searchKeyword.get(key) + "%"
                    ));
                    break;
                default:
                    break;
            }
        }

        return predicate;
    }

}
