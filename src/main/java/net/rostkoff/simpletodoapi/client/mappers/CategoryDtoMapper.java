package net.rostkoff.simpletodoapi.client.mappers;

import net.rostkoff.simpletodoapi.client.contract.CategoryDto;
import net.rostkoff.simpletodoapi.data.model.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryDtoMapper implements IMap<Category, CategoryDto>{
    @Override
    public CategoryDto map(Category entity) {
        var categoryDto = new CategoryDto();
        categoryDto.setId(entity.getId());
        categoryDto.setTitle(entity.getTitle());
        categoryDto.setColour(entity.getColour());
        return categoryDto;
    }
}
