package com.cookery.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ajit on 27/9/17.
 */

public class CommentMO extends CommonMO implements Serializable {
    private int COM_ID;
    private int RCP_ID;
    private String COMMENT;

    private String userImage;
    private boolean userLiked;
    private String userName;

    private int likesCount;

    private List<UserMO> likedUsers;

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public int getCOM_ID() {
        return COM_ID;
    }

    public void setCOM_ID(int COM_ID) {
        this.COM_ID = COM_ID;
    }

    public int getRCP_ID() {
        return RCP_ID;
    }

    public void setRCP_ID(int RCP_ID) {
        this.RCP_ID = RCP_ID;
    }

    public String getCOMMENT() {
        return COMMENT;
    }

    public void setCOMMENT(String COMMENT) {
        this.COMMENT = COMMENT;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isUserLiked() {
        return userLiked;
    }

    public void setUserLiked(boolean userLiked) {
        this.userLiked = userLiked;
    }

    public List<UserMO> getLikedUsers() {
        return likedUsers;
    }

    public void setLikedUsers(List<UserMO> likedUsers) {
        this.likedUsers = likedUsers;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }
}
