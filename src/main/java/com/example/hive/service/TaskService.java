package com.example.hive.service;

import com.example.hive.dto.request.TaskDto;
import com.example.hive.dto.response.ApiResponse;

import java.util.UUID;

public interface TaskService {
    ApiResponse<TaskDto> createTask(TaskDto taskDto);
//    ApiResponse<TaskDto> updateTask(TaskDto taskDto);
    ApiResponse<TaskDto> updateTask(UUID taskId, TaskDto taskDto);
}
