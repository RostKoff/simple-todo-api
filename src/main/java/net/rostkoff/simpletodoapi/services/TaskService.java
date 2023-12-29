package net.rostkoff.simpletodoapi.services;

import net.rostkoff.simpletodoapi.client.contract.CalendarTaskDto;
import net.rostkoff.simpletodoapi.client.contract.TaskDto;
import net.rostkoff.simpletodoapi.client.mappers.CalendarTaskDtoMapper;
import net.rostkoff.simpletodoapi.client.mappers.TaskMapper;
import net.rostkoff.simpletodoapi.data.model.Task;
import net.rostkoff.simpletodoapi.data.repositories.TaskRepository;
import net.rostkoff.simpletodoapi.exceptions.tasks.TaskBadRequest;
import net.rostkoff.simpletodoapi.exceptions.tasks.TaskConflict;
import net.rostkoff.simpletodoapi.exceptions.tasks.TaskNotFound;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.List;

@Service
public class TaskService {
    private final TaskRepository repository;
    private final CalendarTaskDtoMapper calendarDtoMapper;
    private final TaskMapper entityMapper;

    public TaskService(TaskRepository repository, CalendarTaskDtoMapper calendarDtoMapper, TaskMapper entityMapper) {
        this.repository = repository;
        this.calendarDtoMapper = calendarDtoMapper;
        this.entityMapper = entityMapper;
    }

    public List<CalendarTaskDto> getAllTasksBetween(String firstDate, String lastDate) {
        LocalDateTime first, last;
        System.out.println(firstDate);
        try {
            first = LocalDateTime.parse(firstDate, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            last = LocalDateTime.parse(lastDate, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        } catch (DateTimeParseException ex) {
            throw new TaskBadRequest(ex.getMessage());
        }

        if(last.isBefore(first))
            throw new TaskBadRequest("The last date cannot be earlier than the first date");

        var entities = (Collection<Task>) repository.findByStartDateEndDateBetween(first, last);

        if(entities.size() == 0)
            throw new TaskNotFound();

        return entities.stream()
                .map(calendarDtoMapper::map)
                .toList();
    }

    public ResponseEntity<String> addTask(TaskDto taskDto) {
        if(taskDto.getId() != null && repository.existsById(taskDto.getId()))
            throw new TaskConflict("The task you are trying to add already exists");

        var entity = entityMapper.map(taskDto);
        repository.save(entity);
        return ResponseEntity.ok("Task Added");
    }
}
