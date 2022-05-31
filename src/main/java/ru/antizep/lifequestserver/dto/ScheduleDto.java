package ru.antizep.lifequestserver.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class ScheduleDto {

    private int remoteId;
    private String header;
    private String description;
    private boolean daily;
    private String dayOfWeek;
    private String time;
    private int done;
    private int rejected;

}
