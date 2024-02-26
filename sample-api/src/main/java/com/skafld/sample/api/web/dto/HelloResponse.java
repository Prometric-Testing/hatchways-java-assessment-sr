package com.skafld.sample.api.web.dto;

import lombok.Getter;
import lombok.Builder;
import lombok.ToString;
import lombok.AllArgsConstructor;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class HelloResponse {
    private String message;
}
