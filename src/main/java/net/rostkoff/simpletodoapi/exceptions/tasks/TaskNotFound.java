package net.rostkoff.simpletodoapi.exceptions.tasks;

public class TaskNotFound extends RuntimeException {
    public TaskNotFound() {
        super("Task Not Found");
    }
}
