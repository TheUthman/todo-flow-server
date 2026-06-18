package com.uthman.to_do_app.dto.task;

import com.uthman.to_do_app.enums.Priority;
import com.uthman.to_do_app.enums.Status;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class TaskResponse {

    private Long id;

    private String title;

    private String description;

    private Status status;

    private Priority priority;

    private Boolean completed;

    private LocalDate dueDate;

    private Long categoryId;

    private String categoryName;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
