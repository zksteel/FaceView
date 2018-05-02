package org.faceview.gallery.service;

import org.faceview.gallery.entity.Photo;
import org.faceview.gallery.repository.PhotoRepository;
import org.springframework.stereotype.Service;

@Service
public class PhotoServiceImpl implements PhotoService {

    private final PhotoRepository photoRepository;

    public PhotoServiceImpl(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    @Override
    public Photo save(Photo photo) {
        return this.photoRepository.save(photo);
    }
}
