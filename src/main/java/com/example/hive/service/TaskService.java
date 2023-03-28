package com.example.hive.service;

import com.example.hive.dto.request.TaskDto;
import com.example.hive.dto.response.AppResponse;
import com.example.hive.dto.response.TaskResponseDto;
import com.example.hive.entity.User;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.UUID;

public interface TaskService {
    AppResponse<TaskResponseDto> createTask(TaskDto taskDto, HttpServletRequest request);
//    ApiResponse<TaskDto> updateTask(TaskDto taskDto);
AppResponse<TaskResponseDto> updateTask(UUID taskId, TaskDto taskDto);

    List<TaskResponseDto> findAll(int pageNo,int pageSize,String sortBy,String sortDir);

    TaskResponseDto findTaskById(UUID TaskId);

    public String applicationUrl(HttpServletRequest request);

    List<TaskResponseDto> searchTasksBy(String text, int pageNo,int pageSize,String sortBy,String sortDir);

    TaskResponseDto acceptTask(User user, String taskId);
}
