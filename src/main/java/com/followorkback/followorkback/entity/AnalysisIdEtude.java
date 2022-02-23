package com.followorkback.followorkback.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.io.Serializable;

@NoArgsConstructor
public class AnalysisIdEtude implements Serializable {
     private String etudeGeneric;

     private String username;

     public AnalysisIdEtude(String etudeGeneric, String username){
         this.etudeGeneric = etudeGeneric;
         this.username =  username;
     }

}

