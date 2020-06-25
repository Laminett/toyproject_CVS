package com.alliex.cvs.web.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class HelloResponse {

    private final String name;
    private final int amount;

}
