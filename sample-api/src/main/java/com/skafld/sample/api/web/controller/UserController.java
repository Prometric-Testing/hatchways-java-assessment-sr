package com.skafld.sample.api.web.controller;

import com.skafld.sample.api.model.User;
import com.skafld.sample.api.web.dto.UserCreateRequest;
import com.skafld.sample.api.web.dto.UserSearchRequest;
import com.skafld.sample.api.web.dto.UserListResponse;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/v1/user")
public class UserController {

    @RequestMapping(
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public UserListResponse listUsers(
        @Valid final Optional<UserSearchRequest> request) {
        return UserListResponse.builder().userList(List.of()).build();
    }

    @RequestMapping(
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public User createUser(
        @Valid @RequestBody final UserCreateRequest request) {
        return request.toUser();
    }
}
