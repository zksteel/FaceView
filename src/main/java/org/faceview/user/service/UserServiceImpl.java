package org.faceview.user.service;

import org.faceview.user.entity.User;
import org.faceview.user.model.RegisterUserModel;
import org.faceview.user.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final RoleService roleService;

    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, RoleService roleService, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleService = roleService;
        this.modelMapper = modelMapper;
    }

    @Override
    public RegisterUserModel save(RegisterUserModel userModel) {
        User user = this.modelMapper.map(userModel, User.class);
        user.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList(this.roleService.getOneByAuthority("ROLE_USER")));

        this.userRepository.save(user);

        return userModel;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        User user = this.userRepository.getUserByUsername(s);

        if(user == null) throw new UsernameNotFoundException("user not found");

        return user;
    }
}
