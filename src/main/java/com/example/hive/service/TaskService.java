package com.example.hive.service;

import com.example.hive.dto.request.TaskDto;
import com.example.hive.dto.response.ApiResponse;
import com.example.hive.dto.response.TaskResponseDto;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskService {
    ApiResponse<TaskDto> createTask(TaskDto taskDto);
//    ApiResponse<TaskDto> updateTask(TaskDto taskDto);
    ApiResponse<TaskDto> updateTask(UUID taskId, TaskDto taskDto);

    List<TaskResponseDto> findAll(int pageNo,int pageSize,String sortBy,String sortDir);

    TaskResponseDto findTaskById(UUID TaskId);

    List<TaskResponseDto> searchTasksBy(String text);

}
