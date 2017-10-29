package at.works.tasks.service;

import at.works.tasks.domain.Status;
import at.works.tasks.domain.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.BlockingQueue;

public class TaskConsumer implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(TaskConsumer.class);

    private BlockingQueue<Task> taskQueue;
    private TaskService taskService;

    public TaskConsumer(TaskService taskService, BlockingQueue<Task> taskQueue) {
        this.taskService = taskService;
        this.taskQueue = taskQueue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                while (taskQueue.isEmpty()) Thread.sleep(1000);
                Task task = taskQueue.take();

                //PROCESS
                task.setStatus(Status.RUNNING);
                logger.info("Task running: " + task.toString());
                task.setStatus(Status.FINISHED);
                task.setResolvedAt(new Date());

                taskService.updateTask(task);
            }
        } catch (InterruptedException e) {
           logger.error(e.getMessage());
        }
    }
}
