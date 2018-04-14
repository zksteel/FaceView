package org.faceview.user.controller;


import org.faceview.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("users/{substring}/search")
    public ResponseEntity<List> usersSearch(@PathVariable String substring, Principal principal) {

        return new ResponseEntity<List>(this.userService.findUsersWithUsernameContaining(substring, principal.getName()), HttpStatus.OK);
    }

    @PostMapping(value = "/users/{id}/profilePic")
    public String uploadProfImage(@PathVariable String id,
                              @RequestParam("file") MultipartFile multipartFile,
                              Principal principal) {
        this.userService.saveProfilePic(multipartFile, id, id);
        return null;
    }

    @PostMapping(value = "/users/{id}/coverPic")
    public String uploadCoverImage(@PathVariable String id,
                              @RequestParam("file") MultipartFile multipartFile,
                              Principal principal) {
        this.userService.saveCoverPic(multipartFile, id, id);
        return null;
    }
}
