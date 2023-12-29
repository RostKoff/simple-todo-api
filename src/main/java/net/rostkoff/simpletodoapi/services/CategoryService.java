package net.rostkoff.simpletodoapi.services;

import net.rostkoff.simpletodoapi.client.contract.CategoryDto;
import net.rostkoff.simpletodoapi.client.mappers.CategoryDtoMapper;
import net.rostkoff.simpletodoapi.data.model.Category;
import net.rostkoff.simpletodoapi.data.repositories.CategoryRepository;
import net.rostkoff.simpletodoapi.exceptions.categories.CategoryNotFound;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository repository;
    private final CategoryDtoMapper mapper;

    public CategoryService(CategoryRepository repository, CategoryDtoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<CategoryDto> getAllCategories() {
        var entities = (List<Category>) repository.findAll();

        if(entities.isEmpty()) {
            throw new CategoryNotFound();
        }

        return entities.stream().map(mapper::map).toList();
    }
}
