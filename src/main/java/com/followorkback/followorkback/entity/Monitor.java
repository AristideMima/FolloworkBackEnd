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

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String dossierId;

    private String username;

    private String comment;

    @Enumerated(EnumType.STRING)
    private Status status;

}
