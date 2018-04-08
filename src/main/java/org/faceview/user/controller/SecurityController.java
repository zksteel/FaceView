package org.faceview.user.controller;

import org.faceview.user.model.RegisterUserModel;
import org.faceview.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class SecurityController {

    private final UserService userService;

    public SecurityController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterUserModel> registerUser(@RequestBody RegisterUserModel userModel){

        return new ResponseEntity<>(this.userService.save(userModel), HttpStatus.OK);
    }

    @GetMapping("/test")
    public String test(){

        return "you are logged in";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin")
    public String admin(){

        return "you are admin";
    }
}
