package com.cookery.models;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by ajit on 10/10/17.
 */

@Getter
@Setter
public class LikesMO implements Serializable {
    private int USER_ID;
    private int TYPE_ID;
    private String  TYPE;

    private boolean isUserLiked;
    private int likesCount;
}
