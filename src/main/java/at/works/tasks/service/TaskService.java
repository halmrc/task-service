package at.works.tasks.service;

import at.works.tasks.domain.Status;
import at.works.tasks.domain.Task;
import at.works.tasks.storage.TaskRepository;
import at.works.tasks.storage.dao.TaskDao;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

@Service
public class TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ModelMapper modelMapper;

    private BlockingQueue<Task> taskQueue;
    private Thread taskConsumer;

    public TaskService() {
        taskQueue = new PriorityBlockingQueue(1024);
        taskConsumer = new Thread(new TaskConsumer(this, taskQueue));
        taskConsumer.start();
        //ExecutorService executorService = new ThreadPoolExecutor(5, 5, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
    }

    @PreDestroy
    public void shutDown() {
        taskConsumer.interrupt();
    }

    @Scheduled(fixedRate = 10000)
    //@Scheduled(cron = "*/10 * * * * *")
    public void schedule() {
        Task task = createRandomTask();
        TaskDao taskDao = taskRepository.save(modelMapper.map(task, TaskDao.class));
        scheduleTask(modelMapper.map(taskDao, Task.class));
    }

    public void createTask(Task task) {
        taskRepository.save(modelMapper.map(task, TaskDao.class));
        scheduleTask(task);
    }

    public void updateTask(Task task) {
        TaskDao original = taskRepository.findOne(task.getId());
        task.setUpdatedAt(new Date());
        BeanUtils.copyProperties(task, original);
        taskRepository.save(modelMapper.map(task, TaskDao.class));
    }

    public void deleteTask(String taskId) {
        taskRepository.delete(Long.valueOf(taskId));
    }

    public Task getTaskById(String taskId) {
        return modelMapper.map(taskRepository.findOne(Long.valueOf(taskId)), Task.class);
    }


    public  List<Task> getTasksByStatus(Status status) {
        List<Task> tasks = new ArrayList<>();
        for (TaskDao taskDao : taskRepository.findByStatus(status)) {
            tasks.add(modelMapper.map(taskDao, Task.class));
        }
        return tasks;
    }

    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        for (TaskDao taskDao : taskRepository.findAll()) {
            tasks.add(modelMapper.map(taskDao, Task.class));
        }
        return tasks;
    }

    private void scheduleTask(Task task) {
        try {
            taskQueue.put(task);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
    }

    private Date getDueDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        return calendar.getTime();
    }

    protected Task createRandomTask() {
        Task task = new Task();
        task.setUuid(UUID.randomUUID().toString());
        task.setTitle(randomString());
        task.setDescription(randomString());
        task.setCreatedAt(new Date());
        task.setStatus(Status.WAITING);
        task.setPriority(1);
        task.setDueDate(getDueDate());
        return task;
    }

    private String randomString() {
        return  RandomStringUtils.random(5, String.valueOf(System.currentTimeMillis()));
    }

}
