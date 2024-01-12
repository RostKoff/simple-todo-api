package net.rostkoff.simpletodoapi.data.repositories;

import net.rostkoff.simpletodoapi.data.model.Task;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long> {
    @Query("SELECT t FROM Task t WHERE (t.startDate BETWEEN ?1 AND ?2) OR (t.endDate BETWEEN ?1 AND ?2)")
    List<Task> findByStartDateEndDateBetween(LocalDateTime from, LocalDateTime to);
}
