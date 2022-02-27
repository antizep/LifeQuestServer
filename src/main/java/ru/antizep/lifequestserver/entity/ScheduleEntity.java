package ru.antizep.lifequestserver.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Time;

@Entity
@Getter
@Setter
@Table(name = "schedule", schema = "public")
public class ScheduleEntity {

    @Id
    @Column(name="remote_id", nullable=false, updatable=false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long remoteId;
    private String header;
    private String description;
    private boolean daily;
    private String dayOfWeek;
    private Time time;
    private int done;
    private int rejected;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private UserEntity userEntity;
}
