package com.uthman.to_do_app.service.impl;

import com.uthman.to_do_app.dto.task.TaskRequest;
import com.uthman.to_do_app.dto.task.TaskResponse;
import com.uthman.to_do_app.entity.Category;
import com.uthman.to_do_app.entity.Task;
import com.uthman.to_do_app.entity.User;
import com.uthman.to_do_app.enums.Priority;
import com.uthman.to_do_app.enums.Status;
import com.uthman.to_do_app.exception.ResourceNotFoundException;
import com.uthman.to_do_app.repository.CategoryRepository;
import com.uthman.to_do_app.repository.TaskRepository;
import com.uthman.to_do_app.repository.UserRepository;
import com.uthman.to_do_app.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    private User getCurrentUser() {
        String idStr = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            Long id = Long.parseLong(idStr);
            return userRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        } catch (NumberFormatException e) {
            return userRepository.findByUsername(idStr)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        }
    }

    private TaskResponse map(Task task) {
        TaskResponse.TaskResponseBuilder builder = TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .completed(task.getCompleted())
                .dueDate(task.getDueDate())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt());

        if (task.getCategory() != null) {
            builder.categoryId(task.getCategory().getId());
            builder.categoryName(task.getCategory().getName());
        }

        return builder.build();
    }

    @Override
    public List<TaskResponse> getAllTasks() {
        return taskRepository.findByUser(getCurrentUser())
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    @Override
    public TaskResponse getTaskById(Long id) {
        Task task = taskRepository.findByIdAndUser(id, getCurrentUser())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found or unauthorized"));
        return map(task);
    }

    @Override
    public TaskResponse createTask(TaskRequest request) {
        User currentUser = getCurrentUser();

        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findByIdAndUser(request.getCategoryId(), currentUser)
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found or unauthorized"));
        }

        Status status = request.getStatus() != null ? request.getStatus() : Status.TODO;
        boolean completed = (status == Status.COMPLETED);

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .priority(request.getPriority())
                .status(status)
                .dueDate(request.getDueDate())
                .user(currentUser)
                .category(category)
                .completed(completed)
                .build();

        taskRepository.save(task);
        return map(task);
    }

    @Override
    public TaskResponse updateTask(Long id, TaskRequest request) {
        User currentUser = getCurrentUser();
        Task task = taskRepository.findByIdAndUser(id, currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found or unauthorized"));

        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findByIdAndUser(request.getCategoryId(), currentUser)
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found or unauthorized"));
        }

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(request.getPriority());
        
        if (request.getStatus() != null) {
            task.setStatus(request.getStatus());
            task.setCompleted(request.getStatus() == Status.COMPLETED);
        }

        task.setDueDate(request.getDueDate());
        task.setCategory(category);

        taskRepository.save(task);
        return map(task);
    }

    @Override
    public void deleteTask(Long id) {
        Task task = taskRepository.findByIdAndUser(id, getCurrentUser())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found or unauthorized"));
        taskRepository.delete(task);
    }

    @Override
    public List<TaskResponse> getTasksByStatus(Status status) {
        return taskRepository.findByUser(getCurrentUser())
                .stream()
                .filter(task -> task.getStatus() == status)
                .map(this::map)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskResponse> getTasksByPriority(Priority priority) {
        return taskRepository.findByUser(getCurrentUser())
                .stream()
                .filter(task -> task.getPriority() == priority)
                .map(this::map)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskResponse> searchTasks(String keyword) {
        return taskRepository.findByUser(getCurrentUser())
                .stream()
                .filter(task -> task.getTitle().toLowerCase().contains(keyword.toLowerCase()))
                .map(this::map)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskResponse> getOverdueTasks() {
        return taskRepository.findByUser(getCurrentUser())
                .stream()
                .filter(task -> task.getDueDate() != null && task.getDueDate().isBefore(LocalDate.now()))
                .map(this::map)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskResponse> getTasksByCategory(Long categoryId) {
        return taskRepository.findByCategoryIdAndUser(categoryId, getCurrentUser())
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }
}
