package com.uthman.to_do_app.dto.dashboard;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardResponse {

    private long totalTasks;

    private long completedTasks;

    private long pendingTasks;

    private long overdueTasks;

    private double completionRate;
}