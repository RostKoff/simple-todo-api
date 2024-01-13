package net.rostkoff.simpletodoapi.client.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.rostkoff.simpletodoapi.client.contract.TaskDto;
import net.rostkoff.simpletodoapi.data.model.Task;

public class TaskMapperTests {
    private TaskMapper mapper;

    @BeforeEach
    public void init() {
        mapper = new TaskMapper();
    }

    @Test
    public void mapReturnsDto() {
        var taskDto = new TaskDto();
        var date = LocalDateTime.now();
        Task task;

        taskDto.setId(1L);
        taskDto.setTitle("Title");
        taskDto.setDescription("Description");
        taskDto.setStartDate(date);
        taskDto.setEndDate(date);
        taskDto.setCloseDate(date);
        taskDto.setAllDay(false);

        task = mapper.map(taskDto);

        assertEquals(taskDto.getId(), task.getId());
        assertEquals(taskDto.getTitle(), task.getTitle());
        assertEquals(taskDto.getDescription(), task.getDescription());
        assertEquals(taskDto.getStartDate(), task.getStartDate());
        assertEquals(taskDto.getEndDate(), task.getEndDate());
        assertEquals(taskDto.getCloseDate(), task.getCloseDate());
        assertEquals(taskDto.isAllDay(), task.isAllDay());
    }

}