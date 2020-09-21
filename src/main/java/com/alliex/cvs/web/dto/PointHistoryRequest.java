package com.alliex.cvs.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PointHistoryRequest {

    private int pageNumber;

    private String startDate;

    private String endDate;

    private String status;

    private String fullName;

}