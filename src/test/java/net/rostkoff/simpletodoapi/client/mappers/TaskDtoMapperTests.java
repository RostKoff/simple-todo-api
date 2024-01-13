package net.rostkoff.simpletodoapi.client.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.rostkoff.simpletodoapi.client.contract.TaskDto;
import net.rostkoff.simpletodoapi.data.model.Task;

public class TaskDtoMapperTests {
    private TaskDtoMapper mapper;

    @BeforeEach
    public void init() {
        mapper = new TaskDtoMapper();
    }

    @Test
    public void mapReturnsDto() {
        var task = new Task();
        var date = LocalDateTime.now();
        TaskDto taskDto;
        
        task.setId(1L);
        task.setTitle("Title");
        task.setDescription("Description");
        task.setStartDate(date);
        task.setEndDate(date);
        task.setCloseDate(date);
        task.setAllDay(false);

        taskDto = mapper.map(task);

        assertEquals(task.getId(), taskDto.getId());
        assertEquals(task.getTitle(), taskDto.getTitle());
        assertEquals(task.getDescription(), taskDto.getDescription());
        assertEquals(task.getStartDate(), taskDto.getStartDate());
        assertEquals(task.getEndDate(), taskDto.getEndDate());
        assertEquals(task.getCloseDate(), taskDto.getCloseDate());
        assertEquals(task.isAllDay(), taskDto.isAllDay());
    }
}
