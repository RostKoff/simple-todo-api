package net.rostkoff.simpletodoapi.client.contract;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskDto {
    private Long id;
    private String title;
    private String description;
    private String type;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime closeDate;
    private CategoryDto category;
    private boolean allDay;
}
