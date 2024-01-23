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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import net.rostkoff.simpletodoapi.client.contract.CalendarTaskDto;
import net.rostkoff.simpletodoapi.client.contract.TaskDto;
import net.rostkoff.simpletodoapi.exceptions.handlers.TaskExceptionHandler;
import net.rostkoff.simpletodoapi.exceptions.tasks.TaskBadRequest;
import net.rostkoff.simpletodoapi.exceptions.tasks.TaskConflict;
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

    // ?
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

    @Test
    public void getAllCalendarTasksBetweenReturnsBadRequestWhenFirstDateParamIsMissing() throws Exception {
        var lastDateParam = "2024-01-01T01:00:00+01:00";

        mockMvc.perform
        (
            MockMvcRequestBuilders.get("/tasks/calendar/all/between")
                .param("lastDate", lastDateParam)        
        )
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.title").value("Bad Request"))
        .andExpect(status().isBadRequest());
    }

    @Test
    public void getAllCalendarTasksBetweenReturnsBadRequestWhenLastDateParamIsMissing() throws Exception {
        var firstDateParam = "2024-01-01T00:00:00+01:00";

        mockMvc.perform
        (
            MockMvcRequestBuilders.get("/tasks/calendar/all/between")
                .param("firstDate", firstDateParam)        
        )
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.title").value("Bad Request"))
        .andExpect(status().isBadRequest());
    }

    @Test
    public void addTaskReturnsIdWhenTaskIsAdded() throws Exception {
        var expected = ResponseEntity.accepted().body(1L);
        var taskDto = new TaskDto();
        var objectMapper = new ObjectMapper();
        String taskDtoJson;

        taskDto.setId(1L);
        taskDtoJson = objectMapper.writeValueAsString(taskDto);

        when(taskService.addTask(taskDto)).thenReturn(expected);

        mockMvc.perform(
            MockMvcRequestBuilders.post("/tasks/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskDtoJson)
            )
            .andExpect(status().isAccepted())
            .andExpect(jsonPath("$").value(1L));
    }

    @Test
    public void addTaskReturnsConflictWhenTaskWithIdAlreadyExists() throws Exception {
        var taskDto = new TaskDto();
        var objectMapper = new ObjectMapper();
        String taskDtoJson;

        taskDto.setId(1L);
        taskDtoJson = objectMapper.writeValueAsString(taskDto);

        when(taskService.addTask(taskDto)).thenThrow(new TaskConflict("Message"));

        mockMvc.perform(
            MockMvcRequestBuilders.post("/tasks/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskDtoJson)
            )
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.status").value(409))
            .andExpect(jsonPath("$.message").value("Message"));
    }

    @Test
    public void addTaskReturnsBadRequestWhenSaveFunctionFails() throws Exception {
        var taskDto = new TaskDto();
        var objectMapper = new ObjectMapper();
        String taskDtoJson;

        taskDto.setId(1L);
        taskDtoJson = objectMapper.writeValueAsString(taskDto);

        when(taskService.addTask(taskDto)).thenThrow(new TaskBadRequest("Message"));

        mockMvc.perform(
            MockMvcRequestBuilders.post("/tasks/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskDtoJson)
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.message").value("Message"));
    }

    @Test
    public void addTaskReturnsBadRequestWhenJsonIsInvalid() throws Exception {
        var taskDtoJson = "Invalid json";

        mockMvc.perform(
            MockMvcRequestBuilders.post("/tasks/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskDtoJson)
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.title").value("Bad Request"));
    }

    @Test
    public void getTaskReturnsTaskDto() throws Exception {
        var expected = new TaskDto();
        var date = LocalDateTime.now();
        var dateArray = convertDateTimeToArray(date);

        expected.setId(1L);
        expected.setTitle("title");
        expected.setDescription("description");
        expected.setStartDate(date);
        expected.setEndDate(date);
        expected.setCloseDate(date);
        expected.setAllDay(false);

        when(taskService.getTask(1L)).thenReturn(expected);

        mockMvc.perform(
            MockMvcRequestBuilders.get("/tasks/1")
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.title").value("title"))
            .andExpect(jsonPath("$.description").value("description"))
            .andExpect(jsonPath("$.startDate[*]").value(Matchers.contains(dateArray)))
            .andExpect(jsonPath("$.endDate[*]").value(Matchers.contains(dateArray)))
            .andExpect(jsonPath("$.closeDate[*]").value(Matchers.contains(dateArray)))
            .andExpect(jsonPath("$.allDay").value(false));
    }

    @Test
    public void getTaskReturnsNotFoundWhenTaskWithIdDoesNotExist() throws Exception {
        when(taskService.getTask(1L)).thenThrow(new TaskNotFound());

        mockMvc.perform(
            MockMvcRequestBuilders.get("/tasks/1")
            )
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.message").value("Task Not Found"));
    }

    @Test
    public void getTaskReturnsBadRequestWhenIdIsInvalid() throws Exception {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/tasks/invalid")
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.title").value("Bad Request"));
    }

    @Test
    public void deleteTaskReturnsOkWhenTaskIsDeleted() throws Exception {
        when(taskService.deleteTask(1L)).thenReturn(ResponseEntity.ok("Task Deleted"));

        mockMvc.perform(
            MockMvcRequestBuilders.delete("/tasks/1")
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value("Task Deleted"));
    }

    @Test
    public void deleteTaskReturnsNotFoundWhenTaskWithIdDoesNotExist() throws Exception {
        when(taskService.deleteTask(1L)).thenThrow(new TaskNotFound());

        mockMvc.perform(
            MockMvcRequestBuilders.delete("/tasks/1")
            )
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.message").value("Task Not Found"));
    }
    
    @Test
    public void deleteTaskReturnsBadRequestWhenIdIsInvalid() throws Exception {
        mockMvc.perform(
            MockMvcRequestBuilders.delete("/tasks/invalid")
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.title").value("Bad Request"));
    }

    @Test
    public void updateTaskReturnsOkWhenTaskIsUpdated() throws Exception {
        var taskDto = new TaskDto();
        var objectMapper = new ObjectMapper();
        String taskDtoJson;

        taskDto.setId(1L);
        taskDtoJson = objectMapper.writeValueAsString(taskDto);

        when(taskService.updateTask(taskDto)).thenReturn(ResponseEntity.ok("Task Updated"));

        mockMvc.perform(
            MockMvcRequestBuilders.put("/tasks/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskDtoJson)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value("Task Updated"));
    }

    @Test
    public void updateTaskReturnsNotFoundWhenTaskWithIdDoesNotExist() throws Exception {
        var taskDto = new TaskDto();
        var objectMapper = new ObjectMapper();
        String taskDtoJson;

        taskDto.setId(1L);
        taskDtoJson = objectMapper.writeValueAsString(taskDto);

        when(taskService.updateTask(taskDto)).thenThrow(new TaskNotFound());

        mockMvc.perform(
            MockMvcRequestBuilders.put("/tasks/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskDtoJson)
            )
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.message").value("Task Not Found"));
    }

    @Test
    public void updateTaskReturnsBadRequestWhenSaveFunctionFails() throws Exception {
        var taskDto = new TaskDto();
        var objectMapper = new ObjectMapper();
        String taskDtoJson;

        taskDto.setId(1L);
        taskDtoJson = objectMapper.writeValueAsString(taskDto);

        when(taskService.updateTask(taskDto)).thenThrow(new TaskBadRequest("Message"));

        mockMvc.perform(
            MockMvcRequestBuilders.put("/tasks/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskDtoJson)
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.message").value("Message"));
    }

    @Test
    public void updateTaskReturnsBadRequestWhenJsonIsInvalid() throws Exception {
        var taskDtoJson = "Invalid json";

        mockMvc.perform(
            MockMvcRequestBuilders.put("/tasks/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskDtoJson)
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.title").value("Bad Request"));
    }

    private Object[] convertDateTimeToArray(LocalDateTime dateTime) {
        return Arrays.stream(dateTime.toString().split("[\\D]"))
            .map(Integer::valueOf)
            .toArray();
    }
}
