package com.example.hive.service.implementation;

import com.example.hive.dto.request.TaskDto;
import com.example.hive.dto.response.ApiResponse;
import com.example.hive.entity.Task;
import com.example.hive.entity.User;
import com.example.hive.enums.Role;
import com.example.hive.repository.TaskRepository;
import com.example.hive.repository.UserRepository;
import com.example.hive.service.TaskService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Log4j2
@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {
    private TaskRepository taskRepository;
    private UserRepository userRepository;

    @Override
    public ApiResponse<TaskDto> createTask(TaskDto taskDto) {

        // Check if the user has the TASKER role

        String tasker1 = taskDto.getTasker_id();
        log.info("about creating task for: " + tasker1);
        UUID tasker = UUID. fromString(tasker1);

        User user = userRepository.findById(tasker)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!user.getRole().equals(Role.TASKER)) {
            throw new RuntimeException("User is not a TASKER");
        }

        Task task = Task.builder()
                .task_id(taskDto.getTask_id())
                .jobType(taskDto.getJobType())
                .taskDescription(taskDto.getTaskDescription())
                .taskAddress(taskDto.getTaskAddress())
                .taskDeliveryAddress(taskDto.getTaskDeliveryAddress())
                .taskDuration(taskDto.getTaskDuration())
                .budgetRate(taskDto.getBudgetRate())
                .estimatedTime(taskDto.getEstimatedTime())
                .tasker(user)
                .status(taskDto.getStatus())
                .build();

        Task savedTask = taskRepository.save(task);

        return new ApiResponse<>(HttpStatus.CREATED, "Task created successfully", savedTask);
    }

    @Override
    public ApiResponse<TaskDto> updateTask(UUID taskId, TaskDto taskDto) {
        // Check if the user has the DOER role
        UUID doerId = UUID. fromString(taskDto.getDoer_id());

        User doer = userRepository.findById(doerId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!doer.getRole().equals(Role.DOER)) {
            throw new RuntimeException("User is not a DOER");
        }

        // Find and update the task
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        // Update the status of the task
        task.setStatus(taskDto.getStatus());
        task.setDoer(doer);

        Task updatedTask = taskRepository.save(task);

        return new ApiResponse<>(HttpStatus.OK, "Task updated successfully", updatedTask);
    }
}


