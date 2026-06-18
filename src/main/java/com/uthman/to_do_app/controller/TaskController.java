package com.uthman.to_do_app.controller;

import com.uthman.to_do_app.dto.task.TaskRequest;
import com.uthman.to_do_app.dto.task.TaskResponse;
import com.uthman.to_do_app.enums.Priority;
import com.uthman.to_do_app.enums.Status;
import com.uthman.to_do_app.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks() {

        return ResponseEntity.ok(
                taskService.getAllTasks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                taskService.getTaskById(id));
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
            @Valid @RequestBody TaskRequest request) {

        return ResponseEntity.ok(
                taskService.createTask(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequest request) {

        return ResponseEntity.ok(
                taskService.updateTask(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long id) {

        taskService.deleteTask(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<TaskResponse>> getTasksByStatus(
            @PathVariable Status status) {

        return ResponseEntity.ok(
                taskService.getTasksByStatus(status));
    }

    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<TaskResponse>> getTasksByPriority(
            @PathVariable Priority priority) {

        return ResponseEntity.ok(
                taskService.getTasksByPriority(priority));
    }

    @GetMapping("/search")
    public ResponseEntity<List<TaskResponse>> searchTasks(
            @RequestParam String keyword) {

        return ResponseEntity.ok(
                taskService.searchTasks(keyword));
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<TaskResponse>> getOverdueTasks() {

        return ResponseEntity.ok(
                taskService.getOverdueTasks());
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<TaskResponse>> getTasksByCategory(
            @PathVariable Long categoryId) {

        return ResponseEntity.ok(
                taskService.getTasksByCategory(categoryId));
    }
}
