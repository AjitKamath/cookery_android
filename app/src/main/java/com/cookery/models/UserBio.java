package com.cookery.models;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserBio implements Serializable {
    private int USER_BIO_ID;
    private String USER_BIO;
    private String IS_ACTIVE;
    private String CREATE_DTM;
}
