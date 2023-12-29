package net.rostkoff.simpletodoapi.controllers;

import net.rostkoff.simpletodoapi.client.contract.CategoryDto;
import net.rostkoff.simpletodoapi.services.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public Iterable<CategoryDto> getAllCategories() {
        return service.getAllCategories();
    }
}
