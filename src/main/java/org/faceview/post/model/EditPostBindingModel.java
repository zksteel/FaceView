package org.faceview.post.model;

import javax.validation.constraints.NotEmpty;

public class EditPostBindingModel {

    private String id;

    @NotEmpty
    private String content;

    public EditPostBindingModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
