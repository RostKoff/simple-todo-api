package net.rostkoff.simpletodoapi.services;

import net.rostkoff.simpletodoapi.client.contract.TaskDto;
import net.rostkoff.simpletodoapi.client.mappers.TaskDtoMapper;
import net.rostkoff.simpletodoapi.data.model.Task;
import net.rostkoff.simpletodoapi.data.repositories.TaskRepository;
import net.rostkoff.simpletodoapi.exceptions.tasks.TaskBadRequest;
import net.rostkoff.simpletodoapi.exceptions.tasks.TaskNotFound;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository repository;
    private final TaskDtoMapper dtoMapper;

    public TaskService(TaskRepository repository, TaskDtoMapper dtoMapper) {
        this.repository = repository;
        this.dtoMapper = dtoMapper;
    }

    public List<TaskDto> getAllTasksBetween(String firstDate, String lastDate) {
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
                .map(dtoMapper::map)
                .toList();
    }
}
