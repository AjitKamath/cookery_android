package com.cookery.models;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * Created by vishal on 10/10/17.
 */

@Data
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

    private boolean userLiked;

    private int likesCount;
    private int commentsCount;
}
