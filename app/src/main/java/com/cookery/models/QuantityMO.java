package com.cookery.models;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by ajit on 29/8/17.
 */

@Data
public class QuantityMO  implements Serializable {
    private int QTY_ID;
    private String QTY_NAME;
    private String IS_DEF;
}
