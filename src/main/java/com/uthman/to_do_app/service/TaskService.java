package com.uthman.to_do_app.service;

import com.uthman.to_do_app.dto.task.TaskRequest;
import com.uthman.to_do_app.dto.task.TaskResponse;
import com.uthman.to_do_app.enums.Priority;
import com.uthman.to_do_app.enums.Status;

import java.util.List;

public interface TaskService {

    List<TaskResponse> getAllTasks();

    TaskResponse getTaskById(Long id);

    TaskResponse createTask(TaskRequest request);

    TaskResponse updateTask(Long id, TaskRequest request);

    void deleteTask(Long id);

    List<TaskResponse> getTasksByStatus(Status status);

    List<TaskResponse> getTasksByPriority(Priority priority);

    List<TaskResponse> searchTasks(String keyword);

    List<TaskResponse> getOverdueTasks();

    List<TaskResponse> getTasksByCategory(Long categoryId);

}
