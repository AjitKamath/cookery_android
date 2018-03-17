package com.cookery.models;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * Created by ajit on 27/9/17.
 */

@Data
public class CommentMO extends CommonMO implements Serializable {
    private int COM_ID;
    private int RCP_ID;
    private String COMMENT;

    private String userImage;
    private boolean userLiked;
    private String userName;

    private int likesCount;

    private List<UserMO> likedUsers;
}
