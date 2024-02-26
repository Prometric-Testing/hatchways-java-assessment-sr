package com.skafld.sample.api.web.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
public class HelloRequest {
    @NotNull
    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z]*$")
    @Size(min = 1, max = 100)
    private String name;
}
