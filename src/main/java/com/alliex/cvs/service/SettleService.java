package com.alliex.cvs.service;

import com.alliex.cvs.domain.settle.Settle;
import com.alliex.cvs.domain.settle.SettleRepository;
import com.alliex.cvs.domain.settle.SettleSpecification;
import com.alliex.cvs.domain.transaction.TransactionRepository;
import com.alliex.cvs.domain.user.User;
import com.alliex.cvs.exception.SettleNotFoundException;
import com.alliex.cvs.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SettleService {

    private final TransactionRepository transactionRepository;

    private final SettleRepository settleRepository;

    @Transactional(readOnly = true)
    public Page<SettleResponse> getSettleList(Pageable pageable, SettleRequest settleRequest) {
        return settleRepository.findAll(Specification
                .where(StringUtils.isBlank(settleRequest.getAggregatedAt()) ? null : SettleSpecification.withSearchData("aggregatedAt", settleRequest.getAggregatedAt()))
                .and(StringUtils.isBlank(settleRequest.getFullName()) ? null : SettleSpecification.withSearchData("fullName", settleRequest.getFullName())), pageable)
                .map(SettleResponse::new);
    }

    @Transactional
    public Long update(Long id, SettleUpdateRequest request) {
        Settle settle = settleRepository.findById(id)
                .orElseThrow(() -> new SettleNotFoundException("not found settle. id: " + id));

        settle.update(id, request.getStatus(), request.getAdminId());

        return settle.getId();
    }

    @Transactional
    public Integer transactionMonthlySum(LocalDateTime fromDate, LocalDateTime toDate) {
        String aggregatedAt = fromDate.format(DateTimeFormatter.ofPattern("yyyyMM"));

        List<SettleTransMonthlySumRequest> transMonthlySumList = transactionRepository.findByCreatedDate(fromDate, toDate);
        for (SettleTransMonthlySumRequest obj : transMonthlySumList) {
            settleRepository.save(toEntity(obj, aggregatedAt));
        }

        return transMonthlySumList.size();
    }

    private Settle toEntity(SettleTransMonthlySumRequest obj, String aggregatedAt) {
        User setUser = new User();
        setUser.setId(obj.getUserId());

        return Settle.builder()
                .user(setUser)
                .aggregatedAt(aggregatedAt)
                .approvalCount(obj.getApprovalCount().intValue())
                .approvalAmount(obj.getApprovalAmount())
                .cancelCount(obj.getCancelCount().intValue())
                .cancelAmount(obj.getCancelAmount())
                .totalCount(obj.getTotalCount().intValue())
                .totalAmount(obj.getTotalAmount())
                .build();
    }

}