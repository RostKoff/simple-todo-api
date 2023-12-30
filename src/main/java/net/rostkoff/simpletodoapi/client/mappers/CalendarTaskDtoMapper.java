package net.rostkoff.simpletodoapi.client.mappers;

import net.rostkoff.simpletodoapi.client.contract.CalendarTaskDto;
import net.rostkoff.simpletodoapi.data.model.Task;
import org.springframework.stereotype.Component;

@Component
public class CalendarTaskDtoMapper implements IMap<Task, CalendarTaskDto> {
    @Override
    public CalendarTaskDto map(Task entity) {
        var dto = new CalendarTaskDto();
        dto.setId(entity.getId());
        dto.setAllDay(entity.isAllDay());
        dto.setStart(entity.getStartDate());
        dto.setEnd(entity.getEndDate());
        dto.setTitle(entity.getTitle());
        return dto;
    }
}
