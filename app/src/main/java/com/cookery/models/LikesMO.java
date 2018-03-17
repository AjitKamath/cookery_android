package com.cookery.models;

import lombok.Data;

/**
 * Created by ajit on 10/10/17.
 */

@Data
public class LikesMO {
    private int USER_ID;
    private int TYPE_ID;
    private String  TYPE;

    private boolean isUserLiked;
    private int likesCount;
}
