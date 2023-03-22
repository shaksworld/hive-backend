package com.example.hive.controller;

import com.example.hive.constant.AppConstants;
import com.example.hive.constant.ResponseStatus;
import com.example.hive.dto.request.TaskDto;
import com.example.hive.dto.response.ApiResponse;
import com.example.hive.dto.response.AppResponse;
import com.example.hive.dto.response.TaskResponseDto;
import com.example.hive.entity.User;
import com.example.hive.exceptions.ResourceNotFoundException;
import com.example.hive.repository.UserRepository;
import com.example.hive.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private TaskService taskService;
    @Autowired
    private UserRepository userRepository;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/")
//    @PreAuthorize("hasRole('TASKER')")
    public ResponseEntity<AppResponse<TaskResponseDto>> createTask(@Valid @RequestBody TaskDto taskDto) {
        AppResponse<TaskResponseDto> createdTask = taskService.createTask(taskDto);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @PutMapping("/{taskId}")
    public AppResponse<TaskResponseDto> updateTask(
            @PathVariable UUID taskId,
            @RequestBody TaskDto taskDto) {
        return taskService.updateTask(taskId, taskDto);

    }

    @GetMapping(path = "task/details/{taskId}")
    public ResponseEntity<AppResponse<TaskResponseDto>> findTaskById(@PathVariable UUID taskId) {
        TaskResponseDto taskFound = taskService.findTaskById(taskId);

        // creates an ApiResponse object with the retrieved task data
        AppResponse<TaskResponseDto> apiResponse = new AppResponse<>();
        apiResponse.setResult(taskFound);
        apiResponse.setStatusCode(HttpStatus.FOUND.toString()); // a status code indicating success
        apiResponse.setMessage("Task fetched successfully"); // a message describing the response

        // returns an HTTP response with a JSON payload containing the ApiResponse object
        return ResponseEntity.ok().body(apiResponse);
    }

    @GetMapping("task/list")
    public ResponseEntity<AppResponse<Object>> findAllTasks(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        var tasksFound = taskService.findAll(pageNo, pageSize, sortBy, sortDir);

        return ResponseEntity.status(200).body(AppResponse.builder().statusCode("00").isSuccessful(true).result(tasksFound).build());
    }

    //fetch ongoing task(filtered by login doer)
    @GetMapping("/user/completed_task")
    public ResponseEntity<AppResponse<Object>> getUserCompletedTasks(Principal principal) {
        String email = principal.getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        List<TaskResponseDto> doerCompletedTasks = taskService.getUserCompletedTasks(currentUser);
        AppResponse<Object> appResponse = AppResponse.builder()
                .statusCode(ResponseStatus.SUCCESSFUL.getCode())
                .result(doerCompletedTasks)
                .message(ResponseStatus.SUCCESSFUL.getMessage())
                .isSuccessful(true)
                .build();
        return new ResponseEntity<>(appResponse, HttpStatus.OK);

    }

    @GetMapping("/user/ongoing_task")
    public ResponseEntity<AppResponse<Object>> getUserOngoingTasks(Principal principal) {
        String email = principal.getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        List<TaskResponseDto> doerOngoingTasks = taskService.getUserOngoingTasks(currentUser);
        AppResponse<Object> appResponse = AppResponse.builder()
                .statusCode(ResponseStatus.SUCCESSFUL.getCode())
                .result(doerOngoingTasks)
                .message(ResponseStatus.SUCCESSFUL.getMessage())
                .isSuccessful(true)
                .build();
        return new ResponseEntity<>(appResponse, HttpStatus.OK);

    }



}

