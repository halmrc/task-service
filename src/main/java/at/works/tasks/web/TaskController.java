package at.works.tasks.web;

import at.works.tasks.domain.Status;
import at.works.tasks.domain.Task;
import at.works.tasks.service.TaskService;
import at.works.tasks.web.dto.TaskDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class TaskController {

    @Autowired
    private TaskService taskService;
    @Autowired
    private ModelMapper modelMapper;

    @RequestMapping(value = "/")
    public String dashboard(Model model) {
        model.addAttribute("tasks", taskService.getAllTasks().stream()
                                         .map(task -> validateAndMap(task))
                                            .collect(Collectors.toList()));
        return "dashboard";
    }

    @ResponseBody
    @RequestMapping(value = "/tasks/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TaskDto> getAllTasks() throws Exception {
        return taskService.getAllTasks().stream()
                .map(task -> validateAndMap(task))
                    .collect(Collectors.toList());
    }

    @ResponseBody
    @RequestMapping(value = "/tasks/status", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TaskDto> getTasksByStatus(@RequestParam Status status) throws Exception {
        return taskService.getTasksByStatus(status).stream()
                .map(task -> validateAndMap(task))
                .collect(Collectors.toList());
    }

    @ResponseBody
    @RequestMapping(value = "/tasks/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public TaskDto getTaskById(@PathVariable("id") String taskId) throws Exception {
        return validateAndMap(taskService.getTaskById(taskId));
    }

    @RequestMapping(value = "/tasks/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteTaskById(@PathVariable("id") String taskId) throws Exception {
        taskService.deleteTask(taskId);
    }

    @RequestMapping(value = "/tasks/update", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void updateTask(@RequestBody TaskDto taskDto) throws Exception {
        taskService.updateTask(validateAndMap(taskDto));
    }

    @RequestMapping(value = "/tasks/create", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void createTask(@RequestBody TaskDto taskDto) throws Exception {
        taskService.createTask(validateAndMap(taskDto));
    }

    private Task validateAndMap(TaskDto taskDto) {
        return modelMapper.map(taskDto, Task.class);
    }

    private TaskDto validateAndMap(Task task) {
        return modelMapper.map(task, TaskDto.class);
    }

}
