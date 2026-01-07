package com.remake.poki.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api")
public abstract class BaseController {

    protected final String V1 = "v1";
    protected final String V2 = "v2";

    @Value("${spring.application.name}")
    public String appName;

    protected final String publicPatch = "/public";
}
