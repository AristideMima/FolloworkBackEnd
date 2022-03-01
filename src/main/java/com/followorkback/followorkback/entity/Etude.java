package com.followorkback.followorkback.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("etude")
@Table(name = "Etude")
public class Etude extends Dossier {
    private String type;
}
