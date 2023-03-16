package com.example.hive.controller;

import com.example.hive.dto.request.TaskDto;
import com.example.hive.dto.response.ApiResponse;
import com.example.hive.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }
    @PostMapping("/createTask")
    public ResponseEntity<ApiResponse<TaskDto>> createTask(@Valid @RequestBody TaskDto taskDto) {
        ApiResponse<TaskDto> createdTask = taskService.createTask(taskDto);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @PutMapping("/acceptedTask/{taskId}")
    public ApiResponse<TaskDto> updateTask(
            @PathVariable UUID taskId,
            @RequestBody TaskDto taskDto) {
        return taskService.updateTask(taskId, taskDto);

    }
}

