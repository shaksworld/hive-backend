package com.example.hive.service;

import com.example.hive.dto.request.TaskDto;
import com.example.hive.dto.response.ApiResponse;
import com.example.hive.dto.response.AppResponse;
import com.example.hive.dto.response.TaskResponseDto;

import java.util.List;
import java.util.UUID;

public interface TaskService {
    AppResponse<TaskResponseDto> createTask(TaskDto taskDto);
//    ApiResponse<TaskDto> updateTask(TaskDto taskDto);
AppResponse<TaskResponseDto> updateTask(UUID taskId, TaskDto taskDto);

    List<TaskResponseDto> findAll(int pageNo,int pageSize,String sortBy,String sortDir);

    TaskResponseDto findTaskById(UUID TaskId);

}
