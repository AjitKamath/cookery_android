package com.cookery.models;

/**
 * Created by ajit on 8/10/17.
 */

public class UserMO {
    private int user_id;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
