package com.example.hive.service;

import com.example.hive.dto.request.TaskDto;
import com.example.hive.dto.response.AppResponse;
import com.example.hive.dto.response.TaskResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import com.example.hive.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskService {
//    AppResponse<TaskResponseDto> createTask(TaskDto taskDto, HttpServletRequest request);
public AppResponse<TaskResponseDto> createTask(TaskDto taskDto, User user, HttpServletRequest request);
//    ApiResponse<TaskDto> updateTask(TaskDto taskDto);
AppResponse<TaskResponseDto> updateTask(UUID taskId, TaskDto taskDto);

    List<TaskResponseDto> findAll(int pageNo, int pageSize, String sortBy, String sortDir);

    TaskResponseDto findTaskById(UUID TaskId);

    public String applicationUrl(HttpServletRequest request);

    List<TaskResponseDto> searchTasksBy(String text, int pageNo,int pageSize,String sortBy,String sortDir);

    List<TaskResponseDto> getUserCompletedTasks(User currentUser);

    List<TaskResponseDto> getUserOngoingTasks(User currentUser);
    // doer accepted task
    TaskResponseDto acceptTask(User user, String taskId);

}
