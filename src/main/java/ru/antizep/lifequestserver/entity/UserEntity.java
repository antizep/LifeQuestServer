package ru.antizep.lifequestserver.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "user",schema = "public")
public class UserEntity {


    @Id
    @Column(name="id", nullable=false, updatable=false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String username;
    @Column
    private String password;
    @Column
    private String authority;
    @Column
    private boolean enabled;
}
