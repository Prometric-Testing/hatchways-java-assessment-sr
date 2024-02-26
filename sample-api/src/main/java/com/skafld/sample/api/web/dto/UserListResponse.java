package com.skafld.sample.api.web.dto;

import com.skafld.sample.api.model.User;
import lombok.Getter;
import lombok.Builder;
import lombok.ToString;
import lombok.AllArgsConstructor;

import java.util.List;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class UserListResponse {
    private int total;
    private int page;
    private List<User> userList;
}
