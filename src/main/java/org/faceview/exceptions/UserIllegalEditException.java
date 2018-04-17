package org.faceview.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Illegal action")
public class UserIllegalEditException extends RuntimeException{
}
