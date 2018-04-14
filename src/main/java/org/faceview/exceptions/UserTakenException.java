package org.faceview.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Username is already taken")
public class UserTakenException extends RuntimeException {
    public UserTakenException(String username_is_already_taken) {
    }
}
