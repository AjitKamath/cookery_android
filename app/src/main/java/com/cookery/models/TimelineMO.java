package com.cookery.models;

import java.io.Serializable;

/**
 * Created by ajit on 29/10/17.
 */

public class TimelineMO extends CommonMO implements Serializable {
    private int TMLN_ID;
    private String TYPE;
    private int TYPE_ID;

    private String timelineUserName;
    private String timelineUserImage;

    /*recipe*/
    private int recipeId;
    private String recipeName;
    private String recipeTypeName;
    private String recipeCuisineName;
    private int recipeOwnerId;
    private String recipeOwnerName;
    private String recipeOwnerImage;
    private String recipeImage;
    /*recipe*/

    /*comment*/
    private String comment;
    /*comment*/

    /*review*/
    private String review;
    private int rating;
    /*review*/

    public int getTMLN_ID() {
        return TMLN_ID;
    }

    public void setTMLN_ID(int TMLN_ID) {
        this.TMLN_ID = TMLN_ID;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public int getTYPE_ID() {
        return TYPE_ID;
    }

    public void setTYPE_ID(int TYPE_ID) {
        this.TYPE_ID = TYPE_ID;
    }

    public String getTimelineUserName() {
        return timelineUserName;
    }

    public void setTimelineUserName(String timelineUserName) {
        this.timelineUserName = timelineUserName;
    }

    public String getTimelineUserImage() {
        return timelineUserImage;
    }

    public void setTimelineUserImage(String timelineUserImage) {
        this.timelineUserImage = timelineUserImage;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getRecipeTypeName() {
        return recipeTypeName;
    }

    public void setRecipeTypeName(String recipeTypeName) {
        this.recipeTypeName = recipeTypeName;
    }

    public String getRecipeCuisineName() {
        return recipeCuisineName;
    }

    public void setRecipeCuisineName(String recipeCuisineName) {
        this.recipeCuisineName = recipeCuisineName;
    }

    public int getRecipeOwnerId() {
        return recipeOwnerId;
    }

    public void setRecipeOwnerId(int recipeOwnerId) {
        this.recipeOwnerId = recipeOwnerId;
    }

    public String getRecipeOwnerName() {
        return recipeOwnerName;
    }

    public void setRecipeOwnerName(String recipeOwnerName) {
        this.recipeOwnerName = recipeOwnerName;
    }

    public String getRecipeOwnerImage() {
        return recipeOwnerImage;
    }

    public void setRecipeOwnerImage(String recipeOwnerImage) {
        this.recipeOwnerImage = recipeOwnerImage;
    }

    public String getRecipeImage() {
        return recipeImage;
    }

    public void setRecipeImage(String recipeImage) {
        this.recipeImage = recipeImage;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
