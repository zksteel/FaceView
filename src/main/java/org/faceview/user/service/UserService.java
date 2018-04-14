package org.faceview.user.service;

import org.faceview.user.entity.User;
import org.faceview.user.model.RegisterUserModel;
import org.faceview.user.model.UserSearchResultModel;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface UserService extends UserDetailsService {

    RegisterUserModel save(RegisterUserModel userModel);

    List<UserSearchResultModel> findUsersWithUsernameContaining(String substring, String id);

    User findById(String id);

    void saveProfilePic(MultipartFile file, String receiverId, String senderId);

    void saveCoverPic(MultipartFile file, String receiverId, String senderId);
}
