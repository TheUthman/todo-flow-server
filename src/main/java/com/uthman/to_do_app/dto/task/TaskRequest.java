package com.uthman.to_do_app.dto.task;

import com.uthman.to_do_app.enums.Priority;
import com.uthman.to_do_app.enums.Status;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskRequest {

    @NotBlank(message = "Task title is required")
    private String title;

    private String description;

    private Status status;

    private Priority priority;

    private LocalDate dueDate;

    private Long categoryId;
}