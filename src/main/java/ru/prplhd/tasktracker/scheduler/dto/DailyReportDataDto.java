package ru.prplhd.tasktracker.scheduler.dto;

import java.util.List;

public record DailyReportDataDto(
        Long userId,
        String email,
        List<TaskDto> completedTasks,
        List<TaskDto> incompleteTasks
) {
    public record TaskDto(String title, String description) {}
}
