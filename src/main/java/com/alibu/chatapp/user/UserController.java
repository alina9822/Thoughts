package com.alibu.chatapp.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //addusers
    //dissconnectuser

    @GetMapping("/connectedUsers")
    public ResponseEntity<List<UserResponseDto>> findConnectedUsers() {
        List<UserResponseDto> users = userService.getAllUsers();

        return ResponseEntity.ok(users);
    }
}
