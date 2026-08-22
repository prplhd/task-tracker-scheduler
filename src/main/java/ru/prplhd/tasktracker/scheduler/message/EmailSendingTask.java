package ru.prplhd.tasktracker.scheduler.message;

public record EmailSendingTask(
        String recipient,
        String subject,
        String text
) {
}
