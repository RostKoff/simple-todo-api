package net.rostkoff.simpletodoapi.client.mappers;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.rostkoff.simpletodoapi.client.contract.CalendarTaskDto;
import net.rostkoff.simpletodoapi.client.contract.TaskDto;
import net.rostkoff.simpletodoapi.data.model.Task;

@Getter
@Setter
@RequiredArgsConstructor
@Component
public class MapperCatalog implements ICatalogMappers {
    private final IMap<Task, TaskDto> taskDtoMapper;
    private final IMap<Task, CalendarTaskDto> calendarTaskDtoMapper;
    private final IMap<TaskDto, Task> taskMapper;
}
