package com.cookery.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by vishal on 10/10/17.
 */

public class UserMO extends CommonMO implements Serializable {
    private String NAME;
    private String EMAIL;
    private int EMAIL_SCOPE_ID;
    private String PASSWORD;
    private String GENDER;
    private int GENDER_SCOPE_ID;
    private String MOBILE;
    private int MOBILE_SCOPE_ID;
    private String IMG;

    private int followersCount;
    private int followingCount;
    private String newPassword;
    private int recipesCount;
    private boolean following;

    private String emailScopeName;
    private String genderScopeName;
    private String mobileScopeName;

    private String currentRank;
    private List<Milestone> currentRankAndMilestone;
    private List<Milestone> nextRankAndMilestone;

    public int getRecipesCount() {
        return recipesCount;
    }

    public void setRecipesCount(int recipesCount) {
        this.recipesCount = recipesCount;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }

    public String getGENDER() {
        return GENDER;
    }

    public void setGENDER(String GENDER) {
        this.GENDER = GENDER;
    }

    public String getMOBILE() {
        return MOBILE;
    }

    public void setMOBILE(String MOBILE) {
        this.MOBILE = MOBILE;
    }

    public String getIMG() {
        return IMG;
    }

    public void setIMG(String IMG) {
        this.IMG = IMG;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public boolean isFollowing() {
        return following;
    }

    public void setFollowing(boolean following) {
        this.following = following;
    }

    public int getEMAIL_SCOPE_ID() {
        return EMAIL_SCOPE_ID;
    }

    public void setEMAIL_SCOPE_ID(int EMAIL_SCOPE_ID) {
        this.EMAIL_SCOPE_ID = EMAIL_SCOPE_ID;
    }

    public int getGENDER_SCOPE_ID() {
        return GENDER_SCOPE_ID;
    }

    public void setGENDER_SCOPE_ID(int GENDER_SCOPE_ID) {
        this.GENDER_SCOPE_ID = GENDER_SCOPE_ID;
    }

    public int getMOBILE_SCOPE_ID() {
        return MOBILE_SCOPE_ID;
    }

    public void setMOBILE_SCOPE_ID(int MOBILE_SCOPE_ID) {
        this.MOBILE_SCOPE_ID = MOBILE_SCOPE_ID;
    }

    public String getEmailScopeName() {
        return emailScopeName;
    }

    public void setEmailScopeName(String emailScopeName) {
        this.emailScopeName = emailScopeName;
    }

    public String getGenderScopeName() {
        return genderScopeName;
    }

    public void setGenderScopeName(String genderScopeName) {
        this.genderScopeName = genderScopeName;
    }

    public String getMobileScopeName() {
        return mobileScopeName;
    }

    public void setMobileScopeName(String mobileScopeName) {
        this.mobileScopeName = mobileScopeName;
    }

    public List<Milestone> getCurrentRankAndMilestone() {
        return currentRankAndMilestone;
    }

    public void setCurrentRankAndMilestone(List<Milestone> currentRankAndMilestone) {
        this.currentRankAndMilestone = currentRankAndMilestone;
    }

    public List<Milestone> getNextRankAndMilestone() {
        return nextRankAndMilestone;
    }

    public void setNextRankAndMilestone(List<Milestone> nextRankAndMilestone) {
        this.nextRankAndMilestone = nextRankAndMilestone;
    }

    public String getCurrentRank() {
        return currentRank;
    }

    public void setCurrentRank(String currentRank) {
        this.currentRank = currentRank;
    }
}
