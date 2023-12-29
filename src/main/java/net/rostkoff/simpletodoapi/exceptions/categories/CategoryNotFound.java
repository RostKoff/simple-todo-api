package net.rostkoff.simpletodoapi.exceptions.categories;

public class CategoryNotFound extends RuntimeException {
    public CategoryNotFound() {
        super("Category Not Found");
    }
}
