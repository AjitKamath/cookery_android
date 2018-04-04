package com.cookery.models;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * Created by ajit on 27/8/17.
 */

@Data
public class RecipeMO extends CommonMO implements Serializable {
    private int RCP_ID;
    private String RCP_NAME;
    private int FOOD_TYP_ID;
    private int FOOD_CSN_ID;

    private String foodTypeName;
    private String foodCuisineName;

    private List<IngredientAkaMO> ingredients;
    private List<TasteMO> tastes;
    private List<ImageMO> images;
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
    private boolean userFavorite;

    private int likesCount;
    private int viewsCount;
    private int commentsCount;

    // For Context Menu of My List
    private List<MyListMO> mylists;
}
