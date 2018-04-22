package com.cookery.models;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by ajit on 9/10/17.
 */

@Getter
@Setter
public class ReviewMO extends CommonMO implements Serializable {
    private int REV_ID;
    private int RCP_ID;
    private String REVIEW;
    private int RATING;

    private String userImage;
    private boolean userLiked;
    private String userName;
    private boolean userReviewed;

    private String recipeName;
    private String recipeOwnerName;
    private String recipeOwnerImage;
    private String foodTypeName;
    private String foodCuisineName;
    private List<RecipeImageMO> recipeImages;

    private int likesCount;

    private String avgRating;

    private List<UserMO> likedUsers;
}
