package net.rostkoff.simpletodoapi.client.contract;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

// TODO: Check what can be done with groupId

@Getter
@Setter
public class CalendarTaskDto {
    private Long id;
    private boolean allDay;
    private LocalDateTime start;
    private LocalDateTime end;
    private String title;
    private String backgroundColor;
}
