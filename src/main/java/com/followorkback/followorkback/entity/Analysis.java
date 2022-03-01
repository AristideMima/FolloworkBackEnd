package com.followorkback.followorkback.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@DiscriminatorColumn(name = "analysis_type")
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@Data
public abstract class Analysis extends BaseTime {

    private Date date_validation;
    private Date date_reject;
    private Date date_close;
    private Date date_comity_dri;
    private Date date_init_close;
    private Date date_init_suspend;
    private Date date_suspend;
    private long amount_given;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String link;
    private String perspectives;
}
