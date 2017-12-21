package com.cookery.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ajit on 27/8/17.
 */

public class RecipeMO extends CommonMO implements Serializable {
    private int RCP_ID;
    private String RCP_NAME;
    private int FOOD_TYP_ID;
    private int FOOD_CSN_ID;
    private String RCP_PROC;
    private String RCP_PLATING;
    private String RCP_NOTE;

    private String foodTypeName;
    private String foodCuisineName;

    private List<IngredientMO> ingredients;
    private List<TasteMO> tastes;
    private List<String> images;
    private List<CommentMO> comments;
    private List<ReviewMO> reviews;
    private List<String> steps;
    private List<UserMO> likedUsers;
    private List<UserMO> viewedUsers;

    private String avgRating;

    private String userName;
    private String userImage;
    private ReviewMO userReview;
    private boolean userLiked;
    private boolean userReviewed;

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }

    public List<CommentMO> getComments() {
        return comments;
    }

    public void setComments(List<CommentMO> comments) {
        this.comments = comments;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public int getRCP_ID() {
        return RCP_ID;
    }

    public void setRCP_ID(int RCP_ID) {
        this.RCP_ID = RCP_ID;
    }

    public String getFoodCuisineName() {
        return foodCuisineName;
    }

    public void setFoodCuisineName(String foodCuisineName) {
        this.foodCuisineName = foodCuisineName;
    }

    public List<TasteMO> getTastes() {
        return tastes;
    }

    public void setTastes(List<TasteMO> tastes) {
        this.tastes = tastes;
    }

    public List<IngredientMO> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientMO> ingredients) {
        this.ingredients = ingredients;
    }

    public String getRCP_NAME() {
        return RCP_NAME;
    }

    public void setRCP_NAME(String RCP_NAME) {
        this.RCP_NAME = RCP_NAME;
    }

    public int getFOOD_TYP_ID() {
        return FOOD_TYP_ID;
    }

    public void setFOOD_TYP_ID(int FOOD_TYP_ID) {
        this.FOOD_TYP_ID = FOOD_TYP_ID;
    }

    public int getFOOD_CSN_ID() {
        return FOOD_CSN_ID;
    }

    public void setFOOD_CSN_ID(int FOOD_CSN_ID) {
        this.FOOD_CSN_ID = FOOD_CSN_ID;
    }

    public String getRCP_PROC() {
        return RCP_PROC;
    }

    public void setRCP_PROC(String RCP_PROC) {
        this.RCP_PROC = RCP_PROC;
    }

    public String getRCP_PLATING() {
        return RCP_PLATING;
    }

    public void setRCP_PLATING(String RCP_PLATING) {
        this.RCP_PLATING = RCP_PLATING;
    }

    public String getRCP_NOTE() {
        return RCP_NOTE;
    }

    public void setRCP_NOTE(String RCP_NOTE) {
        this.RCP_NOTE = RCP_NOTE;
    }

    public String getFoodTypeName() {
        return foodTypeName;
    }

    public void setFoodTypeName(String foodTypeName) {
        this.foodTypeName = foodTypeName;
    }

    public List<ReviewMO> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewMO> reviews) {
        this.reviews = reviews;
    }

    public ReviewMO getUserReview() {
        return userReview;
    }

    public void setUserReview(ReviewMO userReview) {
        this.userReview = userReview;
    }

    public String getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(String avgRating) {
        this.avgRating = avgRating;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
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

    public boolean isUserReviewed() {
        return userReviewed;
    }

    public void setUserReviewed(boolean userReviewed) {
        this.userReviewed = userReviewed;
    }

    public List<UserMO> getLikedUsers() {
        return likedUsers;
    }

    public void setLikedUsers(List<UserMO> likedUsers) {
        this.likedUsers = likedUsers;
    }

    public List<UserMO> getViewedUsers() {
        return viewedUsers;
    }

    public void setViewedUsers(List<UserMO> viewedUsers) {
        this.viewedUsers = viewedUsers;
    }
}
