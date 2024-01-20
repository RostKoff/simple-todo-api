package net.rostkoff.simpletodoapi.services;

import net.rostkoff.simpletodoapi.client.contract.CalendarTaskDto;
import net.rostkoff.simpletodoapi.client.contract.TaskDto;
import net.rostkoff.simpletodoapi.client.mappers.ICatalogMappers;
import net.rostkoff.simpletodoapi.data.repositories.TaskRepository;
import net.rostkoff.simpletodoapi.exceptions.tasks.TaskBadRequest;
import net.rostkoff.simpletodoapi.exceptions.tasks.TaskConflict;
import net.rostkoff.simpletodoapi.exceptions.tasks.TaskNotFound;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.validation.ConstraintViolationException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class TaskService {
    private final TaskRepository repository;
    private final ICatalogMappers mapperCatalog;

    public TaskService(TaskRepository repository, ICatalogMappers mapperCatalog) {
        this.repository = repository;
        this.mapperCatalog = mapperCatalog;
    }

    public List<CalendarTaskDto> getAllTasksBetween(String firstDate, String lastDate) {
        LocalDateTime first, last;
        try {
            first = LocalDateTime.parse(firstDate, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            last = LocalDateTime.parse(lastDate, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        } catch (DateTimeParseException ex) {
            throw new TaskBadRequest("Invalid date format");
        }

        if(last.isBefore(first))
            throw new TaskBadRequest("The last date cannot be earlier than the first date");

        var entities = repository.findByStartDateEndDateBetween(first, last);

        if(entities.size() == 0)
            throw new TaskNotFound();

        return entities.stream()
                .map(mapperCatalog.getCalendarTaskDtoMapper()::map)
                .toList();
    }

    public ResponseEntity<Long> addTask(TaskDto taskDto) {
        if(taskDto.getId() != null && repository.existsById(taskDto.getId()))
            throw new TaskConflict(String.format("Task with id %d already exists", taskDto.getId()));

        var entity = mapperCatalog.getTaskMapper().map(taskDto);
        try {
            var savedTask = repository.save(entity);
            return ResponseEntity.accepted().body(savedTask.getId());
        } catch(ConstraintViolationException ex) {
            throw new TaskBadRequest("Invalid data format");
        }
    }

    public TaskDto getTask(Long id) {
        if(!repository.existsById(id))
            throw new TaskNotFound();
        return mapperCatalog.getTaskDtoMapper().map(repository.findById(id).get());
    }

    public ResponseEntity<String> deleteTask(Long id) {
        if(!repository.existsById(id))
            throw new TaskNotFound();
        repository.deleteById(id);
        return ResponseEntity.ok("Task Deleted");
    }

    public ResponseEntity<String> updateTask(TaskDto taskDto) {
        if(!repository.existsById(taskDto.getId()))
            throw new TaskNotFound();
        var entity = mapperCatalog.getTaskMapper().map(taskDto);
        try {
            repository.save(entity);
        } catch (ConstraintViolationException ex) {
            throw new TaskBadRequest("Invalid data format");
        }
        return ResponseEntity.ok("Task Updated");
    }
}
