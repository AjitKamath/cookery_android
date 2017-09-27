package com.cookery.models;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by ajit on 27/9/17.
 */

public class CommentMO implements Serializable {
    private int COM_ID;
    private int RCP_ID;
    private int USER_ID;
    private String COMMENT;
    private Date CREATE_DTM;

    private String userImage;
    private int likeCount;

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public int getCOM_ID() {
        return COM_ID;
    }

    public void setCOM_ID(int COM_ID) {
        this.COM_ID = COM_ID;
    }

    public int getRCP_ID() {
        return RCP_ID;
    }

    public void setRCP_ID(int RCP_ID) {
        this.RCP_ID = RCP_ID;
    }

    public int getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(int USER_ID) {
        this.USER_ID = USER_ID;
    }

    public String getCOMMENT() {
        return COMMENT;
    }

    public void setCOMMENT(String COMMENT) {
        this.COMMENT = COMMENT;
    }

    public Date getCREATE_DTM() {
        return CREATE_DTM;
    }

    public void setCREATE_DTM(Date CREATE_DTM) {
        this.CREATE_DTM = CREATE_DTM;
    }
}
