package com.uthman.to_do_app.service.impl;

import com.uthman.to_do_app.dto.dashboard.DashboardResponse;
import com.uthman.to_do_app.entity.User;
import com.uthman.to_do_app.exception.ResourceNotFoundException;
import com.uthman.to_do_app.repository.TaskRepository;
import com.uthman.to_do_app.repository.UserRepository;
import com.uthman.to_do_app.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

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

    @Override
    public DashboardResponse getDashboardStats() {
        User currentUser = getCurrentUser();

        long totalTasks = taskRepository.countByUser(currentUser);

        long completedTasks = taskRepository.countByUserAndCompleted(currentUser, true);

        long pendingTasks = totalTasks - completedTasks;

        long overdueTasks = taskRepository.findByUserAndDueDateBefore(currentUser, LocalDate.now())
                .stream()
                .filter(task -> !Boolean.TRUE.equals(task.getCompleted()))
                .count();

        double completionRate = totalTasks == 0 ? 0 : ((double) completedTasks / totalTasks) * 100;

        return DashboardResponse.builder()
                .totalTasks(totalTasks)
                .completedTasks(completedTasks)
                .pendingTasks(pendingTasks)
                .overdueTasks((int) overdueTasks)
                .completionRate(completionRate)
                .build();
    }
}
