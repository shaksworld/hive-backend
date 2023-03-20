package com.example.hive.controller;

import com.example.hive.constant.AppConstants;
import com.example.hive.dto.request.TaskDto;
import com.example.hive.dto.response.ApiResponse;
import com.example.hive.dto.response.AppResponse;
import com.example.hive.dto.response.TaskResponseDto;
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
    @PostMapping("/")
    public ResponseEntity<ApiResponse<TaskDto>> createTask(@Valid @RequestBody TaskDto taskDto) {
        ApiResponse<TaskDto> createdTask = taskService.createTask(taskDto);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @PutMapping("/{taskId}")
    public ApiResponse<TaskDto> updateTask(
            @PathVariable UUID taskId,
            @RequestBody TaskDto taskDto) {
        return taskService.updateTask(taskId, taskDto);

    }

    @GetMapping(path = "task/details/{taskId}")
    public ResponseEntity<ApiResponse<TaskResponseDto>> findTaskById(@PathVariable UUID taskId) {
        TaskResponseDto taskFound = taskService.findTaskById(taskId);

        // creates an ApiResponse object with the retrieved task data
        ApiResponse<TaskResponseDto> apiResponse = new ApiResponse<>();
        apiResponse.setData(taskFound);
        apiResponse.setStatusCode(HttpStatus.FOUND); // a status code indicating success
        apiResponse.setMessage("Task fetched successfully"); // a message describing the response

        // returns an HTTP response with a JSON payload containing the ApiResponse object
        return ResponseEntity.ok().body(apiResponse);
    }

    @GetMapping( "task/list")
    public ResponseEntity<AppResponse<Object>> findAllTasks(
    @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
    @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
    @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
    @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        TaskResponseDto taskFound = (TaskResponseDto) taskService.findAll(pageNo, pageSize, sortBy, sortDir);

        return ResponseEntity.status(200).body(AppResponse.builder().statusCode("00").isSuccessful(true).result(taskFound).build());
    }

}

