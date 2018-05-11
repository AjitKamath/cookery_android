package com.cookery.models;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HealthMO implements Serializable {
    private String ING_HLTH_IND;
    private String ING_HLTH_DESC;
}
