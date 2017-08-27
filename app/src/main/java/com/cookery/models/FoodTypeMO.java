package com.cookery.models;

import java.io.Serializable;

/**
 * Created by ajit on 26/8/17.
 */

public class FoodTypeMO implements Serializable{
    private int food_typ_id;
    private String food_typ_name;
    private String is_def;

    public String getIs_def() {
        return is_def;
    }

    public void setIs_def(String is_def) {
        this.is_def = is_def;
    }

    public int getFood_typ_id() {
        return food_typ_id;
    }

    public void setFood_typ_id(int food_typ_id) {
        this.food_typ_id = food_typ_id;
    }

    public String getFood_typ_name() {
        return food_typ_name;
    }

    public void setFood_typ_name(String food_typ_name) {
        this.food_typ_name = food_typ_name;
    }
}
