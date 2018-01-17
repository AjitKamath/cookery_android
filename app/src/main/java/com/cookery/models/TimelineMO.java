package com.cookery.models;

import java.io.Serializable;

/**
 * Created by ajit on 29/10/17.
 */

public class TimelineMO extends CommonMO implements Serializable {
    private int TMLN_ID;
    private int REF_USER_ID;
    private String TYPE;
    private int TYPE_ID;

    private String whoName;
    private int whoUserId;
    private String whoUserImage;

    private String whoseName;
    private int whoseUserId;
    private String whoseUserImage;

    /*recipe*/
    private int recipeId;
    private String recipeName;
    private String recipeTypeName;
    private String recipeCuisineName;
    private String recipeOwnerImg;
    private String recipeImage;
    /*recipe*/

    /*comment*/
    private String comment;
    /*comment*/

    /*review*/
    private String review;
    private int rating;
    /*review*/

    private String scopeName;
    private int scopeId;

    public String getScopeName() {
        return scopeName;
    }

    public void setScopeName(String scopeName) {
        this.scopeName = scopeName;
    }

    public int getTMLN_ID() {
        return TMLN_ID;
    }

    public void setTMLN_ID(int TMLN_ID) {
        this.TMLN_ID = TMLN_ID;
    }

    public int getREF_USER_ID() {
        return REF_USER_ID;
    }

    public void setREF_USER_ID(int REF_USER_ID) {
        this.REF_USER_ID = REF_USER_ID;
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

    public String getWhoName() {
        return whoName;
    }

    public void setWhoName(String whoName) {
        this.whoName = whoName;
    }

    public int getWhoUserId() {
        return whoUserId;
    }

    public void setWhoUserId(int whoUserId) {
        this.whoUserId = whoUserId;
    }

    public String getWhoUserImage() {
        return whoUserImage;
    }

    public void setWhoUserImage(String whoUserImage) {
        this.whoUserImage = whoUserImage;
    }

    public String getWhoseName() {
        return whoseName;
    }

    public void setWhoseName(String whoseName) {
        this.whoseName = whoseName;
    }

    public int getWhoseUserId() {
        return whoseUserId;
    }

    public void setWhoseUserId(int whoseUserId) {
        this.whoseUserId = whoseUserId;
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

    public String getRecipeOwnerImg() {
        return recipeOwnerImg;
    }

    public void setRecipeOwnerImg(String recipeOwnerImg) {
        this.recipeOwnerImg = recipeOwnerImg;
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

    public String getWhoseUserImage() {
        return whoseUserImage;
    }

    public void setWhoseUserImage(String whoseUserImage) {
        this.whoseUserImage = whoseUserImage;
    }

    public int getScopeId() {
        return scopeId;
    }

    public void setScopeId(int scopeId) {
        this.scopeId = scopeId;
    }
}
