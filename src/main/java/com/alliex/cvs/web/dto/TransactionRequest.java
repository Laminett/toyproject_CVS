package com.alliex.cvs.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class TransactionRequest {

    private String userId;

    private String paymentType;

    private Long point;

    private String state;

    private String type;

    private String requestId;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate fromDate;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate toDate;

}
