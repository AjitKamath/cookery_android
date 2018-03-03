package com.cookery.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ajit on 9/10/17.
 */

public class ReviewMO extends CommonMO implements Serializable {
    private int REV_ID;
    private int RCP_ID;
    private String REVIEW;
    private int RATING;

    private String userImage;
    private boolean userLiked;
    private String userName;
    private boolean userReviewed;

    private int likesCount;

    private List<UserMO> likedUsers;

    public int getREV_ID() {
        return REV_ID;
    }

    public void setREV_ID(int REV_ID) {
        this.REV_ID = REV_ID;
    }

    public int getRCP_ID() {
        return RCP_ID;
    }

    public void setRCP_ID(int RCP_ID) {
        this.RCP_ID = RCP_ID;
    }

    public String getREVIEW() {
        return REVIEW;
    }

    public void setREVIEW(String REVIEW) {
        this.REVIEW = REVIEW;
    }

    public int getRATING() {
        return RATING;
    }

    public void setRATING(int RATING) {
        this.RATING = RATING;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public boolean isUserReviewed() {
        return userReviewed;
    }

    public void setUserReviewed(boolean userReviewed) {
        this.userReviewed = userReviewed;
    }

    public boolean isUserLiked() {
        return userLiked;
    }

    public void setUserLiked(boolean userLiked) {
        this.userLiked = userLiked;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
