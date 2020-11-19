package com.alliex.cvs.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class SettleRequest {

    private int page;

    @DateTimeFormat(pattern = "yyyyMMdd")
    private LocalDate aggregatedAt;

    private String status;

    private String username;

    private String fullName;

}