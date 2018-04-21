package org.faceview.user.controller;

import org.faceview.user.model.EditUserBindingModel;
import org.faceview.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
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
    public ResponseEntity<List> usersSearchAction(@PathVariable String substring, Principal principal) {

        return new ResponseEntity<>(this.userService.findUsersWithUsernameContaining(substring, principal.getName()), HttpStatus.OK);
    }

    @PostMapping(value = "/users/{id}/profilePic")
    public String uploadProfImage(@PathVariable String id,
                                  @RequestParam("file") MultipartFile multipartFile,
                                  Principal principal) {
        this.userService.saveProfilePic(multipartFile, id, principal);
        return null;
    }

    @PostMapping(value = "/users/{id}/coverPic")
    public String uploadCoverImage(@PathVariable String id,
                                   @RequestParam("file") MultipartFile multipartFile,
                                   Principal principal) {
        this.userService.saveCoverPic(multipartFile, id, principal);
        return null;
    }

    @GetMapping("/friends/all/{id}")
    public ResponseEntity<List> getFriends(@PathVariable String id) {

        return new ResponseEntity<>(this.userService.findAllFriends(id), HttpStatus.OK);
    }

    @GetMapping("/friends/check/{id}")
    public String isFriend(@PathVariable String id, Principal principal) {
        boolean result = this.userService.isFriend(principal.getName(), id);

        if (result) {
            return "{ \"result\": true }";
        } else {
            return "{ \"result\": false }";
        }
    }

    @PostMapping("/users/edit")
    public ResponseEntity editUser(@RequestBody @Valid EditUserBindingModel bindingModel, Principal principal){

        this.userService.editUser(bindingModel, principal.getName());
        return new ResponseEntity(HttpStatus.OK);
    }
}
