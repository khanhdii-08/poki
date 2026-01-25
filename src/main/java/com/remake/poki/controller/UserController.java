package com.remake.poki.controller;

import com.remake.poki.dto.DeductGoldDTO;
import com.remake.poki.dto.LoginDTO;
import com.remake.poki.dto.UpdateStarDTO;
import com.remake.poki.service.UserService;
import com.remake.poki.service.UserSessionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController extends BaseController {

    private final String root = "/user";

    private final UserService userService;
    private final UserSessionService userSessionService;

    @PostMapping(V1 + root + "/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO request, HttpServletRequest httpRequest) {
        return ResponseEntity.ok(userService.login(request, httpRequest));
    }

    @PostMapping(V1 + root)
    public ResponseEntity<?> getUser() {
        return ResponseEntity.ok(userService.getUser());
    }

    @PostMapping(V1 + root + "/find/{username}")
    public ResponseEntity<?> findByUsername(@PathVariable("username") String username) {
        return ResponseEntity.ok(userService.findByUserName(username));
    }

    @PostMapping(V1 + root + "/room/{enemyPetId}")
    public ResponseEntity<?> getInfoRoom(@PathVariable("enemyPetId") Long enemyPetId) {
        return ResponseEntity.ok(userService.getInfoRoom(enemyPetId));
    }

    @PostMapping(V1 + root + "/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok(userSessionService.logout());
    }

    @PostMapping(V1 + root + "/energy")
    public ResponseEntity<?> downEnergy() {
        return ResponseEntity.ok(userService.downEnergy());
    }

    @PostMapping(V1 + root + "/ct/{requestAttack}")
    public ResponseEntity<?> upCT(@PathVariable("requestAttack") int requestAttack) {
        return ResponseEntity.ok(userService.upCT(requestAttack));
    }

    @PostMapping(V1 + root + "/deduct-gold")
    public ResponseEntity<?> deductGold(@RequestBody DeductGoldDTO request) {
        return ResponseEntity.ok(userService.deductGold(request));
    }

    @PostMapping(V1 + root + "/update-star")
    public ResponseEntity<?> updateStar(@RequestBody UpdateStarDTO request) {
        return ResponseEntity.ok(userService.updateStar(request));
    }
}
