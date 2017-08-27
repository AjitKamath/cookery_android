package com.cookery.models;

/**
 * Created by ajit on 27/8/17.
 */

public class RecipeMO {
    private String rcp_name;
    private int food_typ_id;
    private int food_csn_id;
    private int tst_id;
    private String rcp_proc;
    private String rcp_plating;
    private String rcp_note;

    public String getRcp_name() {
        return rcp_name;
    }

    public void setRcp_name(String rcp_name) {
        this.rcp_name = rcp_name;
    }

    public int getFood_typ_id() {
        return food_typ_id;
    }

    public void setFood_typ_id(int food_typ_id) {
        this.food_typ_id = food_typ_id;
    }

    public int getFood_csn_id() {
        return food_csn_id;
    }

    public void setFood_csn_id(int food_csn_id) {
        this.food_csn_id = food_csn_id;
    }

    public int getTst_id() {
        return tst_id;
    }

    public void setTst_id(int tst_id) {
        this.tst_id = tst_id;
    }

    public String getRcp_proc() {
        return rcp_proc;
    }

    public void setRcp_proc(String rcp_proc) {
        this.rcp_proc = rcp_proc;
    }

    public String getRcp_plating() {
        return rcp_plating;
    }

    public void setRcp_plating(String rcp_plating) {
        this.rcp_plating = rcp_plating;
    }

    public String getRcp_note() {
        return rcp_note;
    }

    public void setRcp_note(String rcp_note) {
        this.rcp_note = rcp_note;
    }
}
