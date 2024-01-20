package net.rostkoff.simpletodoapi.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import net.rostkoff.simpletodoapi.client.contract.CalendarTaskDto;
import net.rostkoff.simpletodoapi.client.contract.TaskDto;
import net.rostkoff.simpletodoapi.services.TaskService;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class IntegrationTests {
    private static final String URL = "http://localhost:8081";

    private TaskService taskService;

    @Autowired
    public IntegrationTests(TaskService taskService) {
        this.taskService = taskService;
    }

    @Test
    public void getAllTasksBetweenReturnsListOfTasks() {
        var date = LocalDateTime.of(2021, 1, 1, 0, 0, 0);
        var taskDto = getTaskDto(date);
        var calendarTaskBaseUrl = "/tasks/";
        List<CalendarTaskDto> calendarTasks;
        Optional<CalendarTaskDto> actualTask;
        
        var taskId = taskService.addTask(taskDto).getBody();
        
        try {
            calendarTasks = when()
                                .get(URL + "/tasks/calendar/all/between?firstDate=2021-01-01T00:00:00+01:00&lastDate=2021-01-01T00:00:00+01:00")
                                .then()
                                .statusCode(200)
                                .log()
                                .body()
                                .extract()
                                .jsonPath()
                                .getList("$", CalendarTaskDto.class);
            
            actualTask = calendarTasks.stream()
                .filter(someTask -> someTask.getId().equals(taskId))
                .findFirst();

            assertTrue(actualTask.isPresent());
            assertEquals(taskDto.getTitle(), actualTask.get().getTitle());
            assertEquals(taskDto.getStartDate(),actualTask.get().getStart());
            assertEquals(taskDto.getEndDate(), actualTask.get().getEnd());
            assertEquals(taskDto.isAllDay(), actualTask.get().isAllDay());
            assertEquals(calendarTaskBaseUrl + taskId, actualTask.get().getUrl());
        } finally {
            taskService.deleteTask(taskId);
        }
    }

    @Test
    public void getAllTasksBetweenReturnsListWithoutTaskOutOfRange() {
        var dateOutOfRange = LocalDateTime.of(2021, 1, 2, 0, 0, 0);
        var dateInRange = LocalDateTime.of(2021, 1, 1, 0, 0, 0);
        var taskInRange = getTaskDto(dateInRange);
        var taskOutOfRange = getTaskDto(dateOutOfRange);
        List<CalendarTaskDto> calendarTasks;
        Optional<CalendarTaskDto> actualTask;
        
        var taskIdForInRange = taskService.addTask(taskInRange).getBody();
        var taskIdForOutOfRange = taskService.addTask(taskOutOfRange).getBody();
        
        try {
            calendarTasks = when()
                                .get(URL + "/tasks/calendar/all/between?firstDate=2021-01-01T00:00:00+01:00&lastDate=2021-01-01T00:00:00+01:00")
                                .then()
                                .statusCode(200)
                                .log()
                                .body()
                                .extract()
                                .jsonPath()
                                .getList("$", CalendarTaskDto.class);
            
            actualTask = calendarTasks.stream()
                .filter(someTask -> someTask.getId().equals(taskIdForOutOfRange))
                .findFirst();

            assertTrue(actualTask.isEmpty());
        } finally {
            taskService.deleteTask(taskIdForInRange);
            taskService.deleteTask(taskIdForOutOfRange);
        }
    }

    @Test
    public void getAllTasksBetweenReturnsNotFoundWhenThereIsNoTasks() {
        when()
            .get(URL + "/tasks/calendar/all/between?firstDate=2021-01-02T00:00:00+01:00&lastDate=2021-01-02T00:00:00+01:00")
            .then()
            .statusCode(404);
    }

    @Test
    public void getAllTasksBetweenReturnsBadRequestWhenFirstDateIsAfterLastDate() {
        when()
            .get(URL + "/tasks/calendar/all/between?firstDate=2021-01-02T00:00:00+01:00&lastDate=2021-01-01T00:00:00+01:00")
            .then()
            .statusCode(400);
    }

    @Test
    public void getAllTasksBetweenReturnsBadRequestWhenFirstDateIsNotValid() {
        when()
            .get(URL + "/tasks/calendar/all/between?firstDate=invalid&lastDate=2021-01-01T00:00:00+01:00")
            .then()
            .statusCode(400);
    }

    @Test
    public void getAllTasksBetweenReturnsBadRequestWhenLastDateIsNotValid() {
        when()
            .get(URL + "/tasks/calendar/all/between?firstDate=2021-01-01T00:00:00+01:00&lastDate=invalid")
            .then()
            .statusCode(400);
    }

    @Test
    public void getAllTasksBetweenReturnsBadRequestWhenFirstDateIsNotProvided() {
        when()
            .get(URL + "/tasks/calendar/all/between?lastDate=2021-01-01T00:00:00+01:00")
            .then()
            .statusCode(400);
    }

    @Test
    public void getAllTasksBetweenReturnsBadRequestWhenLastDateIsNotProvided() {
        when()
            .get(URL + "/tasks/calendar/all/between?firstDate=2021-01-01T00:00:00+01:00")
            .then()
            .statusCode(400);
    }

    @Test
    public void getTaskReturnsTask() {
        var date = LocalDateTime.of(2021, 1, 1, 0, 0, 0);
        var taskDto = getTaskDto(date);
        TaskDto actualTask;

        var taskId = taskService.addTask(taskDto).getBody();

        try {
            actualTask = when()
                                .get(URL + "/tasks/" + taskId)
                                .then()
                                .statusCode(200)
                                .log()
                                .body()
                                .extract()
                                .as(TaskDto.class);

            assertEquals(taskId, actualTask.getId());
            assertEquals(taskDto.getTitle(), actualTask.getTitle());
            assertEquals(taskDto.getDescription(), actualTask.getDescription());
            assertEquals(taskDto.getStartDate(), actualTask.getStartDate());
            assertEquals(taskDto.getEndDate(), actualTask.getEndDate());
            assertEquals(taskDto.getCloseDate(), actualTask.getCloseDate());
            assertEquals(taskDto.isAllDay(), actualTask.isAllDay());
        } finally {
            taskService.deleteTask(taskId);
        }
    }

    @Test
    public void getTaskReturnsNotFoundWhenTaskDoesNotExist() {
        when()
            .get(URL + "/tasks/0")
            .then()
            .statusCode(404);
    }

    @Test
    public void getTaskReturnsBadRequestWhenIdIsNotValid() {
        when()
            .get(URL + "/tasks/invalid")
            .then()
            .statusCode(400);
    }

    @Test
    public void addTaskReturnsIdWithAcceptedStatus() {
        var date = LocalDateTime.of(2021, 1, 1, 0, 0, 0);
        var taskDto = getTaskDto(date);

        var taskId = given()
                    .body(taskDto)
                    .headers("Content-Type", "application/json")
                    .when()
                    .request("POST", URL + "/tasks/")
                    .then()
                    .statusCode(202)
                    .extract()
                    .body()
                    .as(Long.class);
            
        taskService.deleteTask(taskId);
    }

    @Test
    public void addTaskReturnsConfictWhenTaskWithIdAlreadyExists() {
        var date = LocalDateTime.of(2021, 1, 1, 0, 0, 0);
        var taskDto = getTaskDto(date);
        
        var taskId = taskService.addTask(taskDto).getBody();
        taskDto.setId(taskId);

        try {
            given()
                .body(taskDto)
                .headers("Content-Type", "application/json")
                .when()
                .request("POST", URL + "/tasks/")
                .then()
                .statusCode(409);
        } finally {
            taskService.deleteTask(taskId);
        }
    }

    @Test
    public void addTaskReturnsBadRequestWhenTaskIsNotValid() {
        var taskDto = "invalid";

        given()
            .body(taskDto)
            .headers("Content-Type", "application/json")
            .when()
            .request("POST", URL + "/tasks/")
            .then()
            .statusCode(400);
    }

    @Test
    public void deleteTaskReturnsOkStatus() {
        var date = LocalDateTime.of(2021, 1, 1, 0, 0, 0);
        var task = getTaskDto(date);

        var taskId = taskService.addTask(task).getBody();

        when()
            .request("DELETE", URL + "/tasks/" + taskId)
            .then()
            .statusCode(200);
    }

    @Test
    public void deleteTaskReturnsNotFoundWhenTaskDoesNotExist() {
        when()
            .request("DELETE", URL + "/tasks/0")
            .then()
            .statusCode(404);
    }

    @Test
    public void deleteTaskReturnsBadRequestWhenIdIsNotValid() {
        when()
            .request("DELETE", URL + "/tasks/invalid")
            .then()
            .statusCode(400);
    }

    @Test
    public void updateTaskReturnsOkStatus() {
        var date = LocalDateTime.of(2021, 1, 1, 0, 0, 0);
        var task = getTaskDto(date);
        
        var taskId = taskService.addTask(task).getBody();
        task.setId(taskId);

        try {
            given()
                .body(task)
                .headers("Content-Type", "application/json")
                .when()
                .request("PUT", URL + "/tasks/")
                .then()
                .statusCode(200);
        } finally {
            taskService.deleteTask(taskId);
        }
    }

    @Test
    public void updateTaskReturnsNotFoundWhenTaskDoesNotExist() {
        var date = LocalDateTime.of(2021, 1, 1, 0, 0, 0);
        var taskDto = getTaskDto(date);
        taskDto.setId(0L);

        given()
            .body(taskDto)
            .headers("Content-Type", "application/json")
            .when()
            .request("PUT", URL + "/tasks/")
            .then()
            .statusCode(404);
    }

    @Test
    public void updateTaskReturnsBadRequestWhenTaskIsNotValid() {
        var taskDto = "invalid";

        given()
            .body(taskDto)
            .headers("Content-Type", "application/json")
            .when()
            .request("PUT", URL + "/tasks/")
            .then()
            .statusCode(400);
    }

    private TaskDto getTaskDto(LocalDateTime date) {
        var taskDto = new TaskDto();

        taskDto.setTitle("Title");
        taskDto.setDescription("Description");
        taskDto.setStartDate(date);
        taskDto.setEndDate(date);
        taskDto.setCloseDate(date);
        taskDto.setAllDay(false);
        return taskDto;
    }
}
