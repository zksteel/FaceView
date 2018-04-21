package org.faceview.gallery.service;

import org.faceview.gallery.entity.Photo;
import org.faceview.user.entity.User;
import org.faceview.user.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GalleryServiceImpl implements GalleryService {

    private final UserService userService;

    public GalleryServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public List<String> findAllPhotos(String username) {
        User user = this.userService.findOneByUsername(username);

        return user.getGallery().getPhotos().stream().map(Photo::getUrl).collect(Collectors.toList());
    }
}
