package net.rostkoff.simpletodoapi.client.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.rostkoff.simpletodoapi.client.contract.CalendarTaskDto;
import net.rostkoff.simpletodoapi.data.model.Task;

public class CalendarTaskDtoMapperTests {
    private CalendarTaskDtoMapper mapper;

    @BeforeEach
    public void init() {
        mapper = new CalendarTaskDtoMapper();
    }

    @Test
    public void mapReturnsDto() {
        var task = new Task();
        var date = LocalDateTime.now();
        CalendarTaskDto calendarTaskDto;
        task.setId(1L);
        task.setTitle("Title");
        task.setStartDate(date);
        task.setEndDate(date);
        task.setAllDay(false);

        calendarTaskDto = mapper.map(task);
        
        assertEquals(task.getId(), calendarTaskDto.getId());
        assertEquals(task.getTitle(), calendarTaskDto.getTitle());
        assertEquals(task.getStartDate(), calendarTaskDto.getStart());
        assertEquals(task.getEndDate(), calendarTaskDto.getEnd());
        assertEquals(task.isAllDay(), calendarTaskDto.isAllDay());
        assertEquals("/tasks/" + task.getId(), calendarTaskDto.getUrl());
    }
}