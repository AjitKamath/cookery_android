package com.cookery.models;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by ajit on 17/3/18.
 */

@Data
public class ImageMO implements Serializable{
    private int RCP_IMG_ID;
    private String RCP_IMG;
    private int RCP_ID;

    private boolean userLiked;

    private int likesCount;
    private int commentsCount;
}
