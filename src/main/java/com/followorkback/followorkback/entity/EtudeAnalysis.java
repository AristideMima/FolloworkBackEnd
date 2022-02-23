package com.followorkback.followorkback.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("etude")
@IdClass(AnalysisIdEtude.class)
@Table(name = "etude_analysis")
public class EtudeAnalysis extends Analysis {
    @Id
    private String etudeGeneric;

    @Id
    private String username;
}
