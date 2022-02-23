package com.followorkback.followorkback.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.*;

@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@DiscriminatorColumn(name = "dossier_type")
@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class Dossier extends BaseTime {
    @Id
    private String genericCode;

    private String unit;

    @Column(unique = true)
    private String name;

    private long neededAmount;
    private String recipients;
    private String domain;
    private Date start_date;
    private Date expected_end_date;

//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name="analyst_id", insertable=true, updatable=true, nullable=false)
    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private User userAnalyst;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private User userManager;

    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(name = "dossier_support", joinColumns = @JoinColumn(name = "generic_code"), inverseJoinColumns = @JoinColumn(name = "\"user_id\""))
    private Collection<User> supports = new ArrayList<>();

    public Collection<User> getSupports(){ return supports; }

    public void setUserManager(User userManager) {
        this.userManager = userManager;
    }

    public void setUserAnalyst(User userAnalyst) {
        this.userAnalyst = userAnalyst;
    }
}
