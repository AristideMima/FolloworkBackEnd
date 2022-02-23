package com.followorkback.followorkback.entity;

import java.io.Serializable;

public class AnalysisIdCredit implements Serializable {
    private Credit credit;
    private User user;

    public AnalysisIdCredit(Credit credit, User user){
        this.credit = credit;
        this.user =  user;
    }

}
