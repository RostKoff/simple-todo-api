package net.rostkoff.simpletodoapi.client.mappers;

import net.rostkoff.simpletodoapi.client.contract.TaskDto;
import net.rostkoff.simpletodoapi.data.model.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskDtoMapper implements IMap<Task, TaskDto> {
    @Override
    public TaskDto map(Task dto) {
        var taskDto = new TaskDto();
        taskDto.setId(dto.getId());
        taskDto.setAllDay(dto.isAllDay());
        taskDto.setStart(dto.getStartDate());
        taskDto.setEnd(dto.getEndDate());
        taskDto.setTitle(dto.getTitle());
        taskDto.setBackgroundColor("#" + dto.getCategory().getColour());
        return taskDto;
    }

}
