package com.cookery.models;

import java.io.Serializable;

/**
 * Created by ajit on 9/9/17.
 */

public class TasteMO  implements Serializable {
    private int TST_ID;
    private String TST_NAME;
    private String IMG;

    private int quantity;

    public int getTST_ID() {
        return TST_ID;
    }

    public void setTST_ID(int TST_ID) {
        this.TST_ID = TST_ID;
    }

    public String getTST_NAME() {
        return TST_NAME;
    }

    public void setTST_NAME(String TST_NAME) {
        this.TST_NAME = TST_NAME;
    }

    public String getIMG() {
        return IMG;
    }

    public void setIMG(String IMG) {
        this.IMG = IMG;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
