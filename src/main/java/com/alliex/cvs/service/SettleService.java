package com.alliex.cvs.service;

import com.alliex.cvs.entity.Settle;
import com.alliex.cvs.repository.SettleRepository;
import com.alliex.cvs.repository.SettleRepositorySupport;
import com.alliex.cvs.repository.TransactionRepository;
import com.alliex.cvs.entity.User;
import com.alliex.cvs.exception.SettleNotFoundException;
import com.alliex.cvs.web.dto.SettleRequest;
import com.alliex.cvs.web.dto.SettleResponse;
import com.alliex.cvs.web.dto.SettleTransMonthlySumRequest;
import com.alliex.cvs.web.dto.SettleUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SettleService {

    private final TransactionRepository transactionRepository;

    private final SettleRepository settleRepository;

    private final SettleRepositorySupport settleRepositorySupport;

    @Transactional(readOnly = true)
    public Page<SettleResponse> getSettleList(Pageable pageable, SettleRequest settleRequest) {
        return settleRepositorySupport.getSettles(pageable, settleRequest).map(SettleResponse::new);
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
        List<SettleTransMonthlySumRequest> transMonthlySumList = transactionRepository.findByCreatedDate(fromDate, toDate);
        for (SettleTransMonthlySumRequest obj : transMonthlySumList) {
            settleRepository.save(toEntity(obj, fromDate.toLocalDate()));
        }

        return transMonthlySumList.size();
    }

    private Settle toEntity(SettleTransMonthlySumRequest obj, LocalDate aggregatedAt) {
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