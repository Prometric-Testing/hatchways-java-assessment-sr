package com.skafld.sample.api.web.controller;

import com.skafld.sample.api.web.dto.HelloRequest;
import com.skafld.sample.api.web.dto.HelloResponse;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("/api/v1/hello/")
public class HelloController {

    @RequestMapping(
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public HelloResponse sayHelloAction(
        @Valid @RequestBody final HelloRequest request) {
        return new HelloResponse(String.format("Hello %s", request.getName()));
    }
}
