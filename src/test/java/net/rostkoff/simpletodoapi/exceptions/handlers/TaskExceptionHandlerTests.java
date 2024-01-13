package net.rostkoff.simpletodoapi.exceptions.handlers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import net.rostkoff.simpletodoapi.exceptions.ErrorResponse;
import net.rostkoff.simpletodoapi.exceptions.tasks.TaskBadRequest;

public class TaskExceptionHandlerTests {
    private TaskExceptionHandler taskExceptionHandler;

    @BeforeEach
    public void init() {
        taskExceptionHandler = new TaskExceptionHandler();
    }

    @Test
    public void handleBadRequestReturnsResponseWithBadRequestStatus() {
        var expected = ResponseEntity.badRequest().body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Message"));
        var actual = taskExceptionHandler.handleBadRequest(new TaskBadRequest("Message"), null);

        assertEquals(expected, actual);
    }

    @Test
    public void handleNotFoundReturnsResponseWithNotFoundStatus() {
        var expected = ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Message"));
        var actual = taskExceptionHandler.handleNotFound(new TaskBadRequest("Message"), null);

        assertEquals(expected, actual);
    }

    @Test
    public void handleConflictReturnsResponseWithConflictStatus() {
        var expected = ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(HttpStatus.CONFLICT.value(), "Message"));
        var actual = taskExceptionHandler.handleConflict(new TaskBadRequest("Message"), null);

        assertEquals(expected, actual);
    }
}
