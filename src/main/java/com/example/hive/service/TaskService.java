package com.example.hive.service;

import com.example.hive.dto.request.TaskDto;
import com.example.hive.dto.response.AppResponse;
import com.example.hive.dto.response.TaskResponseDto;
import com.example.hive.entity.User;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskService {
    AppResponse<TaskResponseDto> createTask(TaskDto taskDto, User user);

    //    ApiResponse<TaskDto> updateTask(TaskDto taskDto);
    AppResponse<TaskResponseDto> updateTask(UUID taskId, TaskDto taskDto);

    List<TaskResponseDto> findAll(int pageNo, int pageSize, String sortBy, String sortDir);

    TaskResponseDto findTaskById(UUID TaskId);
    List<TaskResponseDto> getUserCompletedTasks(User currentUser);

    List<TaskResponseDto> getUserOngoingTasks(User currentUser);
    // doer accepted task
    TaskResponseDto acceptTask(User user, String taskId);

    List<TaskResponseDto> searchTasksBy(String text, int pageNo,int pageSize,String sortBy,String sortDir);

}
