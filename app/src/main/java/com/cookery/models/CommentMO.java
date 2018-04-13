package com.cookery.models;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by ajit on 27/9/17.
 */

@Getter
@Setter
public class CommentMO extends CommonMO implements Serializable {
    private int COM_ID;
    private String TYPE;
    private int TYPE_ID;
    private String COMMENT;

    private String userImage;
    private boolean userLiked;
    private String userName;

    private String recipeImage;

    private int likesCount;

    private List<UserMO> likedUsers;
}
