package org.faceview.user.service;

import org.faceview.user.entity.User;
import org.faceview.user.model.EditUserBindingModel;
import org.faceview.user.model.RegisterUserModel;
import org.faceview.user.model.UserSearchResultModel;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@Service
public interface UserService extends UserDetailsService {

    RegisterUserModel save(RegisterUserModel userModel);

    List<UserSearchResultModel> findUsersWithUsernameContaining(String substring, String id);

    User findById(String id);

    void saveProfilePic(MultipartFile file, String receiverId, Principal principal);

    void saveCoverPic(MultipartFile file, String receiverId, Principal principal);

    List<User> findAllFriends(String userId);

    Boolean isFriend(String userId, String friendId);

    User findOneByUsername(String username);

    void addFriend(User sender, User receiver);

    void editUser(EditUserBindingModel bindingModel, String username);

    List<User> findAll();
}
