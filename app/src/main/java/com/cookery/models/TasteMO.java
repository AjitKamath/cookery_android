package com.cookery.models;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by ajit on 9/9/17.
 */

@Getter
@Setter
public class TasteMO  implements Serializable {
    private int TST_ID;
    private String TST_NAME;
    private String IMG;

    private int quantity;
}
