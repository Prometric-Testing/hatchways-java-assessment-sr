package com.skafld.sample.api.web.dto;

import com.skafld.sample.api.model.User;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class UserCreateRequest {
    @Pattern(regexp = "^[a-zA-Z]*$")
    @Size(min = 0, max = 100)
    private String name;

    public User toUser() {
        return User.builder().name(name).build();
    }
}
