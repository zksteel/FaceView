package org.faceview.user.service;

import org.faceview.user.model.RegisterUserModel;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public interface UserService extends UserDetailsService {

    RegisterUserModel save(RegisterUserModel userModel);
}
