package org.faceview.user.controller;


import org.faceview.user.model.RegisterUserModel;
import org.faceview.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
public class SecurityController {

    private final UserService userService;

    @Autowired
    public SecurityController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterUserModel> registerUser(@Valid @RequestBody RegisterUserModel userModel){

        return new ResponseEntity<>(this.userService.save(userModel), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/admin")
    public String admin(){

        return "you are admin";
    }

}
