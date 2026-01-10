package com.remake.poki.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController extends BaseController {

    @GetMapping(V1 + "/test")
    public String test() {
        return "test";
    }
}
