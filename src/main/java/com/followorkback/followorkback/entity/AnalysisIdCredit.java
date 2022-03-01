package com.followorkback.followorkback.entity;

import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
public class AnalysisIdCredit implements Serializable {
    private String creditGeneric;

    private String username;


    public AnalysisIdCredit(String creditGeneric, String username){
        this.creditGeneric = creditGeneric;
        this.username =  username;
    }

}
