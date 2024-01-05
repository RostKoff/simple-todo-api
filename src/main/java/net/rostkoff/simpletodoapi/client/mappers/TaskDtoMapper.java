package net.rostkoff.simpletodoapi.client.mappers;

import org.springframework.stereotype.Component;

import net.rostkoff.simpletodoapi.client.contract.TaskDto;
import net.rostkoff.simpletodoapi.data.model.Task;

@Component
public class TaskDtoMapper implements IMap<Task, TaskDto> {

    @Override
    public TaskDto map(Task from) {
        var dto = new TaskDto();
        dto.setId(from.getId());
        dto.setTitle(from.getTitle());
        dto.setDescription(from.getDescription());
        dto.setStartDate(from.getStartDate());
        dto.setEndDate(from.getEndDate());
        dto.setCloseDate(from.getCloseDate());
        dto.setAllDay(from.isAllDay());
        return dto;
    }

}
