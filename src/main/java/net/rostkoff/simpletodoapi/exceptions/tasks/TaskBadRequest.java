package net.rostkoff.simpletodoapi.exceptions.tasks;

public class TaskBadRequest extends RuntimeException {
    public TaskBadRequest() {
        super("Task Bad Request");
    }

    public TaskBadRequest(String message) {
        super(message);
    }
}
