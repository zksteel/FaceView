package org.faceview.user.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.faceview.exceptions.UserIllegalEditException;
import org.faceview.exceptions.UserTakenException;
import org.faceview.user.entity.User;
import org.faceview.user.model.EditUserBindingModel;
import org.faceview.user.model.RegisterUserModel;
import org.faceview.user.model.UserSearchResultModel;
import org.faceview.user.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.lang.reflect.Type;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.faceview.config.ApplicationBeanConfig.CLOUD_STORAGE_BUCKET;

@Transactional
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final RoleService roleService;

    private final ModelMapper modelMapper;

    private final Storage storage;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, RoleService roleService, ModelMapper modelMapper, Storage storage) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleService = roleService;
        this.modelMapper = modelMapper;
        this.storage = storage;
    }

    @Override
    public RegisterUserModel save(RegisterUserModel userModel) {
        User search = this.userRepository.getUserByUsername(userModel.getUsername());

        if(search != null){
            throw new UserTakenException("username is already taken");
        }

        User user = this.modelMapper.map(userModel, User.class);
        user.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList(this.roleService.getOneByAuthority("ROLE_USER")));

        this.userRepository.save(user);

        return userModel;
    }

    @Override
    public List<UserSearchResultModel> findUsersWithUsernameContaining(String substring, String username) {

        List<User> userContaining = this.userRepository.findUsersByUsernameContainingAndUsernameIsNot(substring,username);
        Type listType = new TypeToken<List<UserSearchResultModel>>() {}.getType();


        List<UserSearchResultModel> userSearchResultModels = this.modelMapper
                .map(userContaining, listType);

        return userSearchResultModels;
    }

    @Override
    public User findById(String id) {

        return this.userRepository.findById(id).get();
    }

    @Override
    public void saveProfilePic(MultipartFile file, String receiverId, Principal principal) {
        try {

            String senderId = this.userRepository.getUserByUsername(principal.getName()).getId();
            if(!receiverId.equals(senderId)){
                throw new UserIllegalEditException();
            }
            //TODO: Save image to cloud with UUID for name
            //TODO: Clean up code
            BlobId blobId = BlobId.of(CLOUD_STORAGE_BUCKET, senderId + "/" + file.getOriginalFilename());
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("image/jpeg").build();
            Blob blob = this.storage.create(blobInfo, file.getInputStream().readAllBytes());

            String link = this.storage.get(blobId).getMediaLink();
            this.userRepository.updateProfilePic(link, receiverId);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveCoverPic(MultipartFile file, String receiverId, Principal principal) {
        try {
            String senderId = this.userRepository.getUserByUsername(principal.getName()).getId();
            if(!receiverId.equals(senderId)){
                throw new UserIllegalEditException();
            }
            //TODO: Save image to cloud with UUID for name
            BlobId blobId = BlobId.of(CLOUD_STORAGE_BUCKET, senderId + "/" + file.getOriginalFilename());
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("image/jpeg").build();
            Blob blob = this.storage.create(blobInfo, file.getInputStream().readAllBytes());

            String link = this.storage.get(blobId).getMediaLink();
            this.userRepository.updateCoverPic(link, receiverId);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<User> findAllFriends(String userId) {
        return this.userRepository.findAllFriends(userId);
    }

    @Override
    public Boolean isFriend(String username, String friendId) {
        User user = this.userRepository.getUserByUsername(username);
        List<User> allFriends = this.userRepository.findAllFriends(user.getId());

        List<User> friends = allFriends.stream().filter( u -> u.getId().equals(friendId)).collect(Collectors.toList());

        return friends.size() == 1;
    }

    @Override
    public User findOneByUsername(String username) {
        return this.userRepository.getUserByUsername(username);
    }

    @Override
    public void addFriend(User sender, User receiver) {
        List<User> receiverFriends = receiver.getFriends();
        receiverFriends.add(sender);
        receiver.setFriends(receiverFriends);

        List<User> senderFriends = sender.getFriends();
        senderFriends.add(receiver);
        sender.setFriends(senderFriends);

        this.userRepository.save(receiver);
        this.userRepository.save(sender);
    }

    @Override
    public void editUser(EditUserBindingModel bindingModel, String username) {
        User user = this.userRepository.getUserByUsername(username);

        user.setAbout(bindingModel.getAbout());
        user.setTown(bindingModel.getTown());
        user.setAge(bindingModel.getAge());

        this.userRepository.save(user);
    }


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        User user = this.userRepository.getUserByUsername(s);

        if(user == null) throw new UsernameNotFoundException("user not found");

        return user;
    }
}
