package com.cookery.models;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by ajit on 17/3/18.
 */

@Getter
@Setter
public class RecipeImageMO implements Serializable{
    private int RCP_IMG_ID;
    private String RCP_IMG;
    private int RCP_ID;

    private boolean userLiked;

    private int likesCount;
    private int commentsCount;

    private String recipeName;
}
