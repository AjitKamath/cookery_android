package com.cookery.models;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by ajit on 22/1/18.
 */

@Getter
@Setter
public class Milestone implements Serializable{
    private int MLT_ID;
    private int RANK_ID;
    private String TYPE;
    private int NUMBER;

    private String RANK_NAME;
    private int currentNumber;
}
