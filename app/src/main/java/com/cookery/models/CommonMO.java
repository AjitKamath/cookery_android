package com.cookery.models;

import java.io.Serializable;

/**
 * Created by ajit on 22/10/17.
 */

public class CommonMO implements Serializable {
    private int USER_ID;
    private String CREATE_DTM;
    private String MOD_DTM;
    private String IS_DEL;

    public int getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(int USER_ID) {
        this.USER_ID = USER_ID;
    }

    public String getCREATE_DTM() {
        return CREATE_DTM;
    }

    public void setCREATE_DTM(String CREATE_DTM) {
        this.CREATE_DTM = CREATE_DTM;
    }

    public String getMOD_DTM() {
        return MOD_DTM;
    }

    public void setMOD_DTM(String MOD_DTM) {
        this.MOD_DTM = MOD_DTM;
    }

    public String getIS_DEL() {
        return IS_DEL;
    }

    public void setIS_DEL(String IS_DEL) {
        this.IS_DEL = IS_DEL;
    }
}
