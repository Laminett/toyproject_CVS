package com.alliex.cvs.service;

import com.alliex.cvs.domain.settle.Settle;
import com.alliex.cvs.domain.settle.SettleRepository;
import com.alliex.cvs.web.dto.SettleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SettleService {

    private final SettleRepository settleRepository;

    @Transactional(readOnly =  true)
    public List<Integer> getPage(Pageable pageable, String date) {
        Page<Settle> getPage = null;
        getPage = settleRepository.findAll(pageable);

        List<Integer> pages = new ArrayList<>();
        for(int i = 1; i <= getPage.getTotalPages(); i++){
            pages.add(i);
        }

        return pages;
    }

    @Transactional(readOnly = true)
    public List<SettleResponse> getSettleList(Pageable pageable, String date) {
        List<SettleResponse> settleList = null;
        settleList = settleRepository.findAll(pageable).stream().map(SettleResponse::new).collect(Collectors.toList());

        return settleList;
    }

}