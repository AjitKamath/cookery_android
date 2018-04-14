package com.cookery.models;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by ajit on 15/3/18.
 */

@Getter
@Setter
public class TrendMO {
    private int TRND_ID;
    private String TRND_KEY;
    private String TRND_MSG;

    private List<RecipeMO> recipes;
    private List<UserMO> users;
}
