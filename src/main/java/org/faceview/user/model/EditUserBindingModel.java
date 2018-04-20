package org.faceview.user.model;

import javax.validation.constraints.Min;

public class EditUserBindingModel {

    @Min(12)
    private Integer age;

    private String town;

    private String about;

    public EditUserBindingModel() {
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}
