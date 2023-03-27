package com.example.hive.service.implementation;

import com.example.hive.dto.request.TaskDto;
import com.example.hive.dto.response.AppResponse;
import com.example.hive.dto.response.TaskResponseDto;
import com.example.hive.entity.Task;
import com.example.hive.entity.User;
import com.example.hive.enums.Role;
import com.example.hive.enums.Status;
import com.example.hive.exceptions.CustomException;


import com.example.hive.exceptions.ResourceNotFoundException;
import com.example.hive.repository.TaskRepository;
import com.example.hive.repository.UserRepository;
import com.example.hive.service.TaskService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Log4j2
@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {
    private TaskRepository taskRepository;
    private UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public AppResponse<TaskResponseDto> createTask(TaskDto taskDto, User user) {

        // Check if the user has the TASKER role

        if (!user.getRole().equals(Role.TASKER)) {
            throw new RuntimeException("User is not a TASKER");
        }

        Task task = Task.builder()
                .jobType(taskDto.getJobType())
                .taskDescription(taskDto.getTaskDescription())
                .taskAddress(taskDto.getTaskAddress())
                .taskDeliveryAddress(taskDto.getTaskDeliveryAddress())
                .taskDuration(LocalDateTime.parse(taskDto.getTaskDuration()))
                .budgetRate(taskDto.getBudgetRate())
                .estimatedTime(taskDto.getEstimatedTime())
                .tasker(user)
                .status(taskDto.getStatus())
                .build();

        Task savedTask = taskRepository.save(task);


        return AppResponse.buildSuccess(mapToDto(savedTask));
    }

    @Override
    public AppResponse<TaskResponseDto> updateTask(UUID taskId, TaskDto taskDto) {
        // Check if the user has the DOER role

        User doer = userRepository.findById(taskId)
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

        return AppResponse.buildSuccess(mapToDto(updatedTask));
    }

    @Override
    public List<TaskResponseDto> findAll(int pageNo, int pageSize, String sortBy, String sortDir) {
        List<Task> tasks = taskRepository.findAll();
        List<TaskResponseDto> taskList = new ArrayList<>();
        for (Task task : tasks) {
            taskList.add(mapToDto(task));
        }


        return taskList;
    }

    @Override
    public TaskResponseDto findTaskById(UUID taskId) {
        Optional<Task> task = taskRepository.findById(taskId);
        if (task.isPresent()) {
            Task task1 = task.get();
            return mapToDto(task1);
        }

        return null;
    }

    @Override
    public List<TaskResponseDto> getUserCompletedTasks(User currentUser) {
        log.info("fetching doer with id {} completed task ", currentUser.getUser_id());

            List<Task> doerTasks = taskRepository.findCompletedTasksByDoer(currentUser);
            return doerTasks.stream().map(task -> modelMapper.map(task, TaskResponseDto.class)).collect(Collectors.toList());
        }


    @Override
    public List<TaskResponseDto> getUserOngoingTasks(User currentUser) {

            log.info("fetching doer with id {} ongoing task task ", currentUser.getUser_id());
            List<Task> doerTasks = taskRepository.findOngoingTasksByDoer(currentUser);
            return doerTasks.stream().map(task -> modelMapper.map(task, TaskResponseDto.class)).collect(Collectors.toList());

    }


    // doer accepted task
    @Override
    public TaskResponseDto acceptTask(User user, String taskId) {
        Task tasKToUpdate = taskRepository.findById(UUID.fromString(taskId)).orElseThrow(() -> new ResourceNotFoundException("task can not be found"));

        //check if user is doer

        if(!user.getRole().equals(Role.DOER)){
            throw new CustomException("Task is not accessible for this user", HttpStatus.BAD_REQUEST);
        }

        if (isTaskAccepted(tasKToUpdate)) {
            tasKToUpdate.setDoer(user);
            tasKToUpdate.setStatus(Status.ONGOING);
            Task updatedTask = taskRepository.save(tasKToUpdate);
            return modelMapper.map(updatedTask, TaskResponseDto.class);
        }
        throw new CustomException("Task not available", HttpStatus.BAD_REQUEST);
    }

    public boolean isTaskAccepted(Task task) {
        if (task.getStatus().equals(Status.NEW)) {
            return true;
        }
        return false;
    }


    public TaskResponseDto mapToDto(Task task) {

        return TaskResponseDto.builder()
                .jobType(task.getJobType())
                .taskDescription(task.getTaskDescription())
                .taskAddress(task.getTaskAddress())
                .taskDeliveryAddress(task.getTaskDeliveryAddress())
                .taskDuration(task.getTaskDuration().toString())
                .budgetRate(task.getBudgetRate())
                .tasker_id(task.getTasker().getUser_id().toString())
//                .doer_id(task.getDoer().getUser_id().toString())
                .estimatedTime(task.getEstimatedTime())
                .status(task.getStatus())
                .build();
    }




    @Override
    public List<TaskResponseDto> searchTasksBy(String text, int pageNo,int pageSize,String sortBy,String sortDir) {
       Optional<List<Task>> tasksList = taskRepository.searchTasksBy(text);
        List<TaskResponseDto> listOfTasks = new ArrayList<>();

        if(tasksList.isPresent()) {
            for (Task task : tasksList.get()) {
                listOfTasks.add(mapToDto(task));
            }
        } else {
            throw new ResourceNotFoundException("Task not found");
        }

        return listOfTasks;

    }

}


