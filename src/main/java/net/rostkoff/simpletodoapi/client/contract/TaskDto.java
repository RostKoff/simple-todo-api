package net.rostkoff.simpletodoapi.client.contract;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

// TODO: Check what can be done with groupId

@Getter
@Setter
public class TaskDto {
    private Long id;
    private boolean allDay;
    private LocalDateTime start;
    private LocalDateTime end;
    private String title;
    private String backgroundColor;
}
