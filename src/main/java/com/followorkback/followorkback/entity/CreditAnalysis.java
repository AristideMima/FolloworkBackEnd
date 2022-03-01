package com.followorkback.followorkback.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("credit")
@Table(name = "credit_analysis")
@IdClass(AnalysisIdCredit.class)
public class CreditAnalysis extends Analysis {

    @Id
    private String creditGeneric;

    @Id
    private String username;

    private Date date_comity_great;
    private Date date_transmit;
    private Date date_establishment;
}
