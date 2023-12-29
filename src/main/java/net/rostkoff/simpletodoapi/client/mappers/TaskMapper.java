package net.rostkoff.simpletodoapi.client.mappers;

import net.rostkoff.simpletodoapi.client.contract.TaskDto;
import net.rostkoff.simpletodoapi.data.model.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper implements IMap<TaskDto, Task> {
    private final CategoryMapper categoryMapper;

    public TaskMapper(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    @Override
    public Task map(TaskDto dto) {
        var entity = new Task();
        entity.setId(dto.getId());
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setType(dto.getType());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setCloseDate(dto.getCloseDate());
        if(dto.getCategory() != null)
            entity.setCategory(categoryMapper.map(dto.getCategory()));
        entity.setAllDay(dto.isAllDay());
        return entity;
    }
}
