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
    public List<CalendarTaskDto> getAllCalendarTasksBetween(@RequestParam("firstDate") String firstDate, @RequestParam("lastDate") String lastDate) {
        System.out.println("firstDate = " + firstDate + " lastDate = " + lastDate);
        return service.getAllTasksBetween(firstDate, lastDate);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addTask(@RequestBody TaskDto taskDto) {
        return service.addTask(taskDto);
    }
    
    @GetMapping("/{id}")
    public TaskDto getTask(@PathVariable("id") Long id) {
        return service.getTask(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable("id") Long id) {
        return service.deleteTask(id);
    }
}
