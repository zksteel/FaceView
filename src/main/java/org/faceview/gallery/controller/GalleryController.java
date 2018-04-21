package org.faceview.gallery.controller;

import org.faceview.gallery.service.GalleryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class GalleryController {

    private final GalleryService galleryService;

    public GalleryController(GalleryService galleryService) {
        this.galleryService = galleryService;
    }

    @GetMapping("/photos/all")
    public ResponseEntity<List> getPhotos(Principal principal){

        return new ResponseEntity<>(this.galleryService.findAllPhotos(principal.getName()), HttpStatus.OK);
    }
}
