package com.cookery.models;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by ajit on 9/9/17.
 */

@Data
public class TasteMO  implements Serializable {
    private int TST_ID;
    private String TST_NAME;
    private String IMG;

    private int quantity;
}
