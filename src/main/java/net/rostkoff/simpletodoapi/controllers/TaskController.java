package net.rostkoff.simpletodoapi.controllers;

import net.rostkoff.simpletodoapi.client.contract.CalendarTaskDto;
import net.rostkoff.simpletodoapi.client.contract.TaskDto;
import net.rostkoff.simpletodoapi.services.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/calendar/all/between")
    public List<CalendarTaskDto> getAllCalendarTasksBetween(@RequestParam String firstDate, @RequestParam String lastDate) {
        return service.getAllTasksBetween(firstDate, lastDate);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addTask(@RequestBody TaskDto taskDto) {
        return service.addTask(taskDto);
    }
}
