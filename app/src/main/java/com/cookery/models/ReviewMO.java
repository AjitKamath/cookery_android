package com.cookery.models;

import java.io.Serializable;

/**
 * Created by ajit on 9/10/17.
 */

public class ReviewMO extends CommonMO implements Serializable {
    private int REV_ID;
    private int RCP_ID;
    private String REVIEW;
    private int RATING;

    private String userImage;
    private int likeCount;
    private boolean isReviewed;
    private String rating;
    private boolean userLiked;
    private String name;

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

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public boolean isReviewed() {
        return isReviewed;
    }

    public void setReviewed(boolean reviewed) {
        isReviewed = reviewed;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public boolean isUserLiked() {
        return userLiked;
    }

    public void setUserLiked(boolean userLiked) {
        this.userLiked = userLiked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
