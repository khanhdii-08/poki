package com.remake.poki.controller;

import com.remake.poki.dto.LoginDTO;
import com.remake.poki.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController extends BaseController {

    private final String root = "/user";

    private final UserService userService;

    @PostMapping(root + V1 + "/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO request, HttpServletRequest httpRequest) {
        return ResponseEntity.ok(userService.login(request, httpRequest));
    }
}
