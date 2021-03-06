package org.faceview.post.model;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;

public class CreatePostBindingModel {

    @NotEmpty
    private String content;

    private MultipartFile image;

    public CreatePostBindingModel() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }
}
