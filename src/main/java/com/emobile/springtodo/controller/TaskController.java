package com.emobile.springtodo.controller;

import com.emobile.springtodo.api.swagger.ITaskController;
import com.emobile.springtodo.dto.input.TaskRequest;
import com.emobile.springtodo.dto.mapper.TaskRequestMapper;
import com.emobile.springtodo.dto.mapper.TaskResponseMapper;
import com.emobile.springtodo.dto.output.TaskResponse;
import com.emobile.springtodo.entity.Task;
import com.emobile.springtodo.service.TaskService;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController extends AbstractController<Task, TaskResponse, TaskRequest> implements ITaskController {

    private final TaskService taskService;

    public TaskController(TaskRequestMapper requestMapper, TaskResponseMapper responseMapper,
                          TaskService entityService, MeterRegistry meterRegistry) {
        super(requestMapper, responseMapper, meterRegistry, entityService);
        this.taskService = entityService;
    }

    @GetMapping("/user/{id}")
    public List<TaskResponse> getTasksForUser(@PathVariable long id, @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size) {
        meterRegistry.counter("request.count").increment();
        return taskService.findAllForUser(id, page, size).stream().map(responseMapper::toDto).toList();
    }
}
