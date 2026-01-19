package com.remake.poki.controller;

import com.remake.poki.dto.OnlineUserDTO;
import com.remake.poki.service.OnlineUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OnlineUserController extends BaseController {
    private final String root = "/online-user";

    private final OnlineUserService onlineUserService;

    @PostMapping(root + V1 + "/online")
    public List<OnlineUserDTO> getAvailableOnlineUsersExcludeCurrentUser() {
        return onlineUserService.getAvailableOnlineUsersExcludeCurrentUser();
    }
}
