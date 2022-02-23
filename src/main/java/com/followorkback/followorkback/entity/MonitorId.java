package com.followorkback.followorkback.entity;

import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
public class MonitorId  implements Serializable {

    private String dossierId;
    private String username;

    public MonitorId (String dossierId, String username){
        this.dossierId = dossierId;
        this.username = username;
    }

}
