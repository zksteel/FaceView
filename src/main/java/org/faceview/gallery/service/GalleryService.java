package org.faceview.gallery.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GalleryService {

    List<String> findAllPhotos(String username);
}
