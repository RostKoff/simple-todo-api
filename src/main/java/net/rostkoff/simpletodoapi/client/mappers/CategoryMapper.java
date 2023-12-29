package net.rostkoff.simpletodoapi.client.mappers;

import net.rostkoff.simpletodoapi.client.contract.CategoryDto;
import net.rostkoff.simpletodoapi.data.model.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper implements IMap<CategoryDto, Category>{
    @Override
    public Category map(CategoryDto dto) {
        var entity = new Category();
        entity.setId(dto.getId());
        entity.setTitle(dto.getTitle());
        entity.setColour(dto.getColour());

        return entity;
    }
}
