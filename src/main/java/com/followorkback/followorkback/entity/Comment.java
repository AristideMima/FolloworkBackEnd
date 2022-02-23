package com.followorkback.followorkback.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.rmi.server.UID;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Comment")
public class Comment extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String genericCode;
    private String username;

    @Lob
    private String comment;

    @OneToOne
    private Comment parentId;
}
