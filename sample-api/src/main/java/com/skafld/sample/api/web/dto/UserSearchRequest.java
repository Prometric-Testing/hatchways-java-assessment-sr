package com.skafld.sample.api.web.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Builder;
import lombok.ToString;
import lombok.AllArgsConstructor;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class UserSearchRequest {
    @Pattern(regexp = "^[a-zA-Z]*$")
    @Size(min = 0, max = 100)
    private String name;
}
