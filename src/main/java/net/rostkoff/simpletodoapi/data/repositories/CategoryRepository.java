package net.rostkoff.simpletodoapi.data.repositories;

import net.rostkoff.simpletodoapi.data.model.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Long> {
}
