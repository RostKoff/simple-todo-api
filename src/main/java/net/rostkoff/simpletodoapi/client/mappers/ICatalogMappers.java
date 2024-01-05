package net.rostkoff.simpletodoapi.client.mappers;

import net.rostkoff.simpletodoapi.client.contract.CalendarTaskDto;
import net.rostkoff.simpletodoapi.client.contract.TaskDto;
import net.rostkoff.simpletodoapi.data.model.Task;

public interface ICatalogMappers {
    IMap<TaskDto, Task> getTaskMapper();
    IMap<Task, TaskDto> getTaskDtoMapper();
    IMap<Task, CalendarTaskDto> getCalendarTaskDtoMapper();
}
