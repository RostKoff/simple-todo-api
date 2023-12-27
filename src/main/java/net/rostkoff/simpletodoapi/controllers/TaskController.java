package net.rostkoff.simpletodoapi.controllers;

import net.rostkoff.simpletodoapi.client.contract.TaskDto;
import net.rostkoff.simpletodoapi.data.model.Task;
import net.rostkoff.simpletodoapi.services.TaskService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/all/between")
    public List<TaskDto> getAllTasksBetween(@RequestParam String firstDate, @RequestParam String lastDate) {
        return service.getAllTasksBetween(firstDate, lastDate);
    }
}
