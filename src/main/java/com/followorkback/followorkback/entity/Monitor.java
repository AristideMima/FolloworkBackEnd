package com.followorkback.followorkback.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(MonitorId.class)
public class Monitor extends BaseTime {
    @Id
    private String dossierId;

    @Id
    private String username;

    private String comment;

    @Enumerated(EnumType.STRING)
    private Status status;

}
