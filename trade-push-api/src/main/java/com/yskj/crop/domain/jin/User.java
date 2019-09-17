package com.yskj.crop.domain.jin;

import java.util.List;

/**
 * Created with IDEA
 * author:lhl
 * Date:2019/2/19 0019
 * Time:10:57
 */
public class User {
    private Integer id;
    private String username;
    private String password;
    private Integer state;

    private String image;
    public User() {
    }

    public User(Integer id, String username, String password, Integer state) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.state = state;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


}
