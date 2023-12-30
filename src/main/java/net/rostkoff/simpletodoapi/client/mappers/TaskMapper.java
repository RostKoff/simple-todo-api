package net.rostkoff.simpletodoapi.client.mappers;

import net.rostkoff.simpletodoapi.client.contract.TaskDto;
import net.rostkoff.simpletodoapi.data.model.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper implements IMap<TaskDto, Task> {
    @Override
    public Task map(TaskDto dto) {
        var entity = new Task();
        entity.setId(dto.getId());
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setCloseDate(dto.getCloseDate());
        entity.setAllDay(dto.isAllDay());
        return entity;
    }
}
