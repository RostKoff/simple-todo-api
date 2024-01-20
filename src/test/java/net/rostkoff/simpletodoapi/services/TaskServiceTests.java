package net.rostkoff.simpletodoapi.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import jakarta.validation.ConstraintViolationException;
import net.rostkoff.simpletodoapi.client.contract.CalendarTaskDto;
import net.rostkoff.simpletodoapi.client.contract.TaskDto;
import net.rostkoff.simpletodoapi.client.mappers.CalendarTaskDtoMapper;
import net.rostkoff.simpletodoapi.client.mappers.ICatalogMappers;
import net.rostkoff.simpletodoapi.client.mappers.TaskDtoMapper;
import net.rostkoff.simpletodoapi.client.mappers.TaskMapper;
import net.rostkoff.simpletodoapi.data.model.Task;
import net.rostkoff.simpletodoapi.data.repositories.TaskRepository;
import net.rostkoff.simpletodoapi.exceptions.tasks.TaskBadRequest;
import net.rostkoff.simpletodoapi.exceptions.tasks.TaskConflict;
import net.rostkoff.simpletodoapi.exceptions.tasks.TaskNotFound;

public class TaskServiceTests {
    @Mock
    private TaskRepository repository;
    @Mock
    private ICatalogMappers mapperCatalog;
    @InjectMocks
    private TaskService service;

    private AutoCloseable ac;

    @BeforeEach
    public void init() {
        ac = MockitoAnnotations.openMocks(this);
    }
    
    @AfterEach
    public void tearDown() throws Exception {
        ac.close();
    }

    @Test
    public void addTaskReturnsIdWithStatus202() {
        var task = new Task();
        ResponseEntity<Long> expected = ResponseEntity.accepted().body(1L),
        actual;
        var taskMapper = Mockito.mock(TaskMapper.class);

        task.setId(1L);
        
        Mockito.when(mapperCatalog.getTaskMapper()).thenReturn(taskMapper);
        Mockito.when(taskMapper.map(Mockito.any(TaskDto.class))).thenReturn(task);
        Mockito.when(repository.save(task)).thenReturn(task);
        actual = service.addTask(new TaskDto());

        assertEquals(expected, actual);
    }

    @Test
    public void addTaskSavesTaskIntoRepository() {
        var task = new Task();
        var taskMapper = Mockito.mock(TaskMapper.class);
        var captor = ArgumentCaptor.forClass(Task.class);
        
        task.setId(1L);

        Mockito.when(mapperCatalog.getTaskMapper()).thenReturn(taskMapper);
        Mockito.when(taskMapper.map(Mockito.any(TaskDto.class))).thenReturn(task);
        Mockito.when(repository.save(task)).thenReturn(task);
        service.addTask(new TaskDto());

        verify(repository, times(1)).save(captor.capture());
        assertEquals(task, captor.getValue());
    }

    @Test
    public void addTaskThrowsConflictExceptionIfTaskWithIdAlreadyExists() {
        var task = new TaskDto();

        task.setId(1L);

        Mockito.when(repository.existsById(1L)).thenReturn(true);
        
        assertThrows(TaskConflict.class, () -> {
            service.addTask(task);
        });
    }

    @Test
    public void addTaskThrowsBadRequestExceptionIfTaskHasInvalidData() {
        var task = new Task();
        var taskMapper = Mockito.mock(TaskMapper.class);
        
        task.setId(1L);

        Mockito.when(mapperCatalog.getTaskMapper()).thenReturn(taskMapper);
        Mockito.when(taskMapper.map(Mockito.any(TaskDto.class))).thenReturn(task);
        Mockito.when(repository.save(task)).thenThrow(new ConstraintViolationException(null));
        
        assertThrows(TaskBadRequest.class, () -> {
            service.addTask(new TaskDto());
        });
    }

    @Test
    public void getTaskReturnsTaskDto() {
        var task = new Task();
        var taskMapper = Mockito.mock(TaskDtoMapper.class);
        TaskDto expected = new TaskDto(), 
        actual;

        expected.setId(1L);

        Mockito.when(repository.existsById(1L)).thenReturn(true);
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(task));
        Mockito.when(mapperCatalog.getTaskDtoMapper()).thenReturn(taskMapper);
        Mockito.when(taskMapper.map(task)).thenReturn(expected);

        actual = service.getTask(1L);
        assertEquals(expected, actual);
    }

    @Test
    public void getTaskThrowsNotFoundExceptionIfTaskWithIdDoesNotExist() {
        Mockito.when(repository.existsById(1L)).thenReturn(false);
        
        assertThrows(TaskNotFound.class, () -> {
            service.getTask(1L);
        });
    }

    @Test
    public void deleteTaskReturnsOkResponse() {
        ResponseEntity<String> expected = ResponseEntity.ok("Task Deleted"),
        actual;

        Mockito.when(repository.existsById(1L)).thenReturn(true);
        actual = service.deleteTask(1L);
        assertEquals(expected, actual);
    }

    @Test
    public void deleteTaskDeletesTaskFromRepository() {
        var captor = ArgumentCaptor.forClass(Long.class);

        Mockito.when(repository.existsById(1L)).thenReturn(true);
        service.deleteTask(1L);

        verify(repository, times(1)).deleteById(captor.capture());
        assertEquals(1L, captor.getValue());
    }

    @Test
    public void deleteTaskThrowsNotFoundExceptionIfTaskWithIdDoesNotExist() {
        Mockito.when(repository.existsById(1L)).thenReturn(false);
        
        assertThrows(TaskNotFound.class, () -> {
            service.deleteTask(1L);
        });
    }

    @Test
    public void updateTaskReturnsOkResponse() {
        ResponseEntity<String> expected = ResponseEntity.ok("Task Updated"),
        actual;
        var taskMapper = Mockito.mock(TaskMapper.class);

        Mockito.when(repository.existsById(Mockito.any())).thenReturn(true);
        Mockito.when(mapperCatalog.getTaskMapper()).thenReturn(taskMapper);

        actual = service.updateTask(new TaskDto());

        assertEquals(expected, actual);
    }

    @Test
    public void updateTaskUpdatesTaskInRepository() {
        var task = new Task();
        var taskDto = new TaskDto();        
        var taskMapper = Mockito.mock(TaskMapper.class);
        var captor = ArgumentCaptor.forClass(Task.class);

        task.setId(1L);
        
        Mockito.when(repository.existsById(Mockito.any())).thenReturn(true);
        Mockito.when(mapperCatalog.getTaskMapper()).thenReturn(taskMapper);
        Mockito.when(taskMapper.map(taskDto)).thenReturn(task);

        service.updateTask(taskDto);

        verify(repository, times(1)).save(captor.capture());
        assertEquals(task, captor.getValue());
    }

    @Test
    public void updateTaskThrowsNotFoundExceptionIfTaskWithIdDoesNotExist() {
        Mockito.when(repository.existsById(1L)).thenReturn(false);
        
        assertThrows(TaskNotFound.class, () -> {
            service.updateTask(new TaskDto());
        });
    }

    @Test void updateTaskThrowsBadRequestExceptionIfTaskHasInvalidData() {
        var task = new Task();
        var taskDto = new TaskDto();        
        var taskMapper = Mockito.mock(TaskMapper.class);

        task.setId(1L);
        
        Mockito.when(repository.existsById(Mockito.any())).thenReturn(true);
        Mockito.when(mapperCatalog.getTaskMapper()).thenReturn(taskMapper);
        Mockito.when(taskMapper.map(taskDto)).thenReturn(task);
        Mockito.when(repository.save(task)).thenThrow(new ConstraintViolationException(null));
        
        assertThrows(TaskBadRequest.class, () -> {
            service.updateTask(taskDto);
        });
    }

    @Test
    public void getAllTasksBetweenThrowsBadRequestExceptionIfFirstDateIsInvalid() {
        assertThrows(TaskBadRequest.class, () -> {
            service.getAllTasksBetween("invalid", "2023-01-01T00:00:00+01:00");
        });
    }

    @Test
    public void getAllTasksBetweenThrowsBadRequestExceptionIfLastDateIsInvalid() {
        assertThrows(TaskBadRequest.class, () -> {
            service.getAllTasksBetween("2023-01-01T00:00:00+01:00", "invalid");
        });
    }

    @Test
    public void getAllTasksBetweenThrowsBadRequestExceptionIfLastDateIsBeforeFirstDate() {
        assertThrows(TaskBadRequest.class, () -> {
            service.getAllTasksBetween("2023-01-02T00:00:00+01:00", "2023-01-01T00:00:00+01:00");
        });
    }

    @Test
    public void getAllTasksBetweenThrowsNotFoundExceptionIfNoTasksFound() {
        Mockito.when(repository.findByStartDateEndDateBetween(Mockito.any(), Mockito.any())).thenReturn(Collections.emptyList());
        
        assertThrows(TaskNotFound.class, () -> {
            service.getAllTasksBetween("2023-01-01T00:00:00+01:00", "2023-01-02T00:00:00+01:00");
        });
    }

    @Test
    public void getAllTasksBetweenReturnsListOfCalendarTaskDto() {
        var task = new Task();
        var calendarTaskDtoMapper = Mockito.mock(CalendarTaskDtoMapper.class);
        var calendarTaskDto = new CalendarTaskDto();
        var expected = List.of(calendarTaskDto);
        List<CalendarTaskDto> actual;

        Mockito.when(repository.findByStartDateEndDateBetween(Mockito.any(), Mockito.any())).thenReturn(List.of(task));
        Mockito.when(mapperCatalog.getCalendarTaskDtoMapper()).thenReturn(calendarTaskDtoMapper);
        Mockito.when(calendarTaskDtoMapper.map(task)).thenReturn(calendarTaskDto);

        actual = service.getAllTasksBetween("2023-01-01T00:00:00+01:00", "2023-01-02T00:00:00+01:00");
        assertEquals(expected, actual);
    }



    /* TODO: 
        - Ask about dependency call verification
        - Ask about nulls in tests.
        - Ask about tests with exceptions in controller.
    */


}
