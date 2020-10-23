package com.alliex.cvs.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SettleRequest {

    private int page;

    private String aggregatedAt;

    private String status;

    private String username;

    private String fullName;

}