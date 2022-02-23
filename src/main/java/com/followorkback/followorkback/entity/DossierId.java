package com.followorkback.followorkback.entity;

import java.io.Serializable;

public class DossierId implements Serializable {
    private String genericCode;;
    private User userManager;

    public DossierId(String genericCode, User userManager){
        this.genericCode = genericCode;
        this.userManager = userManager;
    }
}
