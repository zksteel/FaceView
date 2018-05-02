package org.faceview.gallery.service;

import org.faceview.gallery.entity.Photo;
import org.springframework.stereotype.Service;

@Service
public interface PhotoService {
    Photo save(Photo photo);
}
