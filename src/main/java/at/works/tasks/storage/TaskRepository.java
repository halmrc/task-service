package at.works.tasks.storage;

import at.works.tasks.domain.Status;
import at.works.tasks.storage.dao.TaskDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<TaskDao, Long> {

        List<TaskDao> findByTitle(String title);

        List<TaskDao> findByStatus(Status status);
}
