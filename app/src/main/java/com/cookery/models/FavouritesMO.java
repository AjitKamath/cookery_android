package com.cookery.models;

/**
 * Created by v!shal on 03/02/18.
 */

public class FavouritesMO {
    private int USER_ID;
    private int RCP_ID;
    private boolean fabStatus;

    public int getUSER_ID() {
        return USER_ID;

    }

    public void setUSER_ID(int USER_ID) {
        this.USER_ID = USER_ID;
    }

    public int getRCP_ID() {
        return RCP_ID;
    }

    public void setRCP_ID(int RCP_ID) {
        this.RCP_ID = RCP_ID;
    }

    public boolean isFabStatus() {
        return fabStatus;
    }


    public void setFabStatus(boolean fabStatus) {
        this.fabStatus = fabStatus;
    }

}
