package org.faceview.user.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.faceview.exceptions.UserIllegalEditException;
import org.faceview.exceptions.UserTakenException;
import org.faceview.gallery.entity.Gallery;
import org.faceview.gallery.entity.Photo;
import org.faceview.gallery.service.PhotoService;
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
import java.util.UUID;
import java.util.stream.Collectors;

import static org.faceview.config.ApplicationBeanConfig.CLOUD_STORAGE_BUCKET;

@Transactional
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final RoleService roleService;

    private final ModelMapper modelMapper;

    private final PhotoService photoService;

    private final Storage storage;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, RoleService roleService, ModelMapper modelMapper, PhotoService photoService, Storage storage) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleService = roleService;
        this.modelMapper = modelMapper;
        this.photoService = photoService;
        this.storage = storage;
    }

    @Override
    public RegisterUserModel save(RegisterUserModel userModel) {
        User search = this.userRepository.getUserByUsername(userModel.getUsername());

        if (search != null) {
            throw new UserTakenException("username is already taken");
        }

        User user = this.modelMapper.map(userModel, User.class);
        user.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList(this.roleService.getOneByAuthority("ROLE_USER")));

        //Creating default empty gallery for every registered user
        Gallery gallery = new Gallery();
        gallery.setUser(user);
        user.setGallery(gallery);

        this.userRepository.save(user);
        return userModel;
    }

    @Override
    public List<UserSearchResultModel> findUsersWithUsernameContaining(String substring, String loggedInUsername) {

        List<User> userContaining = this.userRepository.findUsersByUsernameContainingAndUsernameIsNot(substring, loggedInUsername);
        Type listType = new TypeToken<List<UserSearchResultModel>>() {
        }.getType();

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

            User user = this.userRepository.getUserByUsername(principal.getName());
            if (!receiverId.equals(user.getId())) {
                throw new UserIllegalEditException();
            }
            //TODO: Save image with its won file extension
            String link = this.uploadAndSave(user, file);

            user.setProfilePic(link);
            this.userRepository.save(user);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveCoverPic(MultipartFile file, String receiverId, Principal principal) {
        try {
            User user = this.userRepository.getUserByUsername(principal.getName());
            if (!receiverId.equals(user.getId())) {
                throw new UserIllegalEditException();
            }
            //TODO: Save image to cloud with UUID for name
            String link = this.uploadAndSave(user, file);

            user.setCoverPic(link);
            this.userRepository.save(user);

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

        List<User> friends = allFriends.stream().filter(u -> u.getId().equals(friendId)).collect(Collectors.toList());

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
    public List<User> findAll() {
        return this.userRepository.findAll();
    }


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        User user = this.userRepository.getUserByUsername(s);

        if (user == null) throw new UsernameNotFoundException("user not found");

        return user;
    }

    //returns link to the uploaded image
    private String uploadAndSave(User user, MultipartFile file) throws IOException {
        //Assign UUID for file name
        BlobId blobId = BlobId.of(CLOUD_STORAGE_BUCKET, user.getId() + "/" + UUID.randomUUID());
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("image/jpeg").build();
        Blob blob = this.storage.create(blobInfo, file.getInputStream().readAllBytes());
        String link = this.storage.get(blobId).getMediaLink();

        Photo photo = new Photo();
        photo.setGallery(user.getGallery());
        photo.setUrl(link);
        photo.setBlobId(blobId.getName());
        this.photoService.save(photo);

        return photo.getUrl();
    }
}
