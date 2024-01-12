package net.rostkoff.simpletodoapi.controllers;

import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import net.rostkoff.simpletodoapi.client.contract.CalendarTaskDto;
import net.rostkoff.simpletodoapi.exceptions.handlers.TaskExceptionHandler;
import net.rostkoff.simpletodoapi.exceptions.tasks.TaskBadRequest;
import net.rostkoff.simpletodoapi.exceptions.tasks.TaskNotFound;
import net.rostkoff.simpletodoapi.services.TaskService;



public class TaskControllerTests {
    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    private MockMvc mockMvc;
    private AutoCloseable ac;

    @BeforeEach
    void init() {
        ac = MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new TaskExceptionHandler(), taskController).build();
    }

    @AfterEach
    void tearDown() throws Exception {
        ac.close();
    }

    @Test
    public void getAllCalendarTasksBetweenReturnsListOfCalendarTaskDto() throws Exception {
        String firstDateParam = "2024-01-01T00:00:00+01:00",
                lastDateParam = "2024-01-01T01:00:00+01:00";
        LocalDateTime start = LocalDateTime.now(),
                end = LocalDateTime.now().plusHours(1);
        var expectedStart = convertDateTimeToArray(start);
        var expectedEnd = convertDateTimeToArray(end);
        var calendarTaskDto = new CalendarTaskDto();

        calendarTaskDto.setId(1L);
        calendarTaskDto.setTitle("title");
        calendarTaskDto.setStart(start);
        calendarTaskDto.setEnd(end);
        calendarTaskDto.setAllDay(false);
        calendarTaskDto.setUrl("/tasks/1");
        
        List<CalendarTaskDto> expected = List.of(calendarTaskDto);
        when(taskService.getAllTasksBetween(firstDateParam, lastDateParam)).thenReturn(expected);

        mockMvc.perform
        (
            MockMvcRequestBuilders.get("/tasks/calendar/all/between")
                .param("firstDate", firstDateParam)
                .param("lastDate", lastDateParam)        
        )
        .andExpect(jsonPath("$[0].id").value(1L))
        .andExpect(jsonPath("$[0].title").value("title"))
        .andExpect(jsonPath("$[0].start[*]").value(Matchers.contains(expectedStart)))
        .andExpect(jsonPath("$[0].end[*]").value(Matchers.contains(expectedEnd)))
        .andExpect(jsonPath("$[0].allDay").value(false))
        .andExpect(jsonPath("$[0].url").value("/tasks/1"))
        .andExpect(status().isOk());
    }

    @Test
    public void getAllCalendarTasksBetweenReturnsBadRequestWhenExceptionIsThrown() throws Exception {
        String firstDateParam = "",
                lastDateParam = "";
        when(taskService.getAllTasksBetween(firstDateParam, lastDateParam)).thenThrow(new TaskBadRequest("Message"));

        mockMvc.perform
        (
            MockMvcRequestBuilders.get("/tasks/calendar/all/between")
                .param("firstDate", firstDateParam)
                .param("lastDate", lastDateParam)        
        )
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.message").value("Message"))
        .andExpect(status().isBadRequest());
    }

    @Test
    public void getAllCalendarTasksBetweenReturnsNotFoundWhenExceptionIsThrown() throws Exception {
        String firstDateParam = "",
                lastDateParam = "";
        when(taskService.getAllTasksBetween(firstDateParam, lastDateParam)).thenThrow(new TaskNotFound());

        mockMvc.perform
        (
            MockMvcRequestBuilders.get("/tasks/calendar/all/between")
                .param("firstDate", firstDateParam)
                .param("lastDate", lastDateParam)        
        )
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.message").value("Task Not Found"))
        .andExpect(status().isNotFound());
    }

    private Object[] convertDateTimeToArray(LocalDateTime dateTime) {
        return Arrays.stream(dateTime.toString().split("[\\D]"))
            .map(Integer::valueOf)
            .toArray();
    }
}
