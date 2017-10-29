package at.works.tasks.storage;

import at.works.tasks.domain.Status;
import at.works.tasks.storage.dao.TaskDao;
import at.works.tasks.web.dto.TaskDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<TaskDao, UUID> {

        TaskDao findOne(UUID uuid);

        void delete(UUID uuid);

        List<TaskDao> findByTitle(String title);

        List<TaskDao> findByStatus(Status status);
}
