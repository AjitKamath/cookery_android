package com.cookery.models;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by ajit on 29/10/17.
 */

@Getter
@Setter
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
    private List<RecipeImageMO> recipeImage;
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
}
