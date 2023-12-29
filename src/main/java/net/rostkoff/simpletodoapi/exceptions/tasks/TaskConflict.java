package net.rostkoff.simpletodoapi.exceptions.tasks;

public class TaskConflict extends RuntimeException {
    public TaskConflict() {
        super("Task Conflict");
    }

    public TaskConflict(String message) {
        super(message);
    }
}
