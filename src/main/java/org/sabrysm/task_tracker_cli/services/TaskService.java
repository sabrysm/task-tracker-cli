package org.sabrysm.task_tracker_cli.services;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.google.gson.Gson;

import org.sabrysm.task_tracker_cli.models.Task;
import org.sabrysm.task_tracker_cli.repositories.TaskRepository;

public class TaskService {
    private final TaskRepository repository = new TaskRepository();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:SS");
    private final int idWidth = 5;
    private final int descriptionWidth = 40;
    private final int statusWidth = 15;
    private final int createdAtWidth = 23;
    private final int updatedAtWidth = 23;

    public void addTask(String description) throws IOException {
        int id = repository.nextId();
        Task newTask = new Task(
                id,
                description,
                "todo",
                LocalDateTime.now().format(formatter),
                null
        );

        // Add the task
        repository.addTask(newTask);
        System.out.println("Task added successfully (ID: " + id + ").");
    }

    public void updateTask(int id, String newDescription) throws IOException {
        if (!repository.isTaskExists(id)) {
            System.out.println("Task with " + id + " not found.");
            return;
        }
        Task task = repository.getTask(id);
        repository.updateTask(id, newDescription, task.getStatus());
        System.out.println("Task " + id + " has been updated successfully.");
    }

    public void deleteTask(int id) throws IOException {
        if (!repository.isTaskExists(id)) {
            System.out.println("Task with " + id + " not found.");
            return;
        }
        repository.deleteTask(id);
        System.out.println("Task " + id + " has been deleted successfully.");
    }

    public void markTask(int id, String status) throws IOException {
        if (!repository.isTaskExists(id)) {
            System.out.println("Task with " + id + " not found.");
            return;
        }
        Task task = repository.getTask(id);
        repository.updateTask(id, task.getDescription(), status);
        System.out.println("Task " + id + " has been marked as " + status + ".");
    }

    public void listTasks() throws IOException {
        List<Task> tasks = repository.getTasks();

        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
            return;
        }

        // Print the header with equal spacing
        System.out.printf("%-" + idWidth + "s%-" + descriptionWidth + "s%-" + statusWidth + "s%-" + createdAtWidth + "s%-" + updatedAtWidth + "s\n",
                "ID", "Description", "Status", "Created At", "Updated At");

        // Print a separator line
        System.out.println("-".repeat(idWidth + descriptionWidth + statusWidth + createdAtWidth + updatedAtWidth));

        // Print the task data rows with equal column spacing
        for (Task task : tasks) {
            // Wrap the description if it's too long in new lines
            String description = task.getDescription();
            for (int i = 0; i < description.length(); i += descriptionWidth - 3) {
                System.out.printf("%-" + idWidth + "s%-" + descriptionWidth + "s%-" + statusWidth + "s%-" + createdAtWidth + "s%-" + updatedAtWidth + "s\n",
                        i == 0 ? task.getId() : "",
                        description.substring(i, Math.min(i + descriptionWidth - 3, description.length())),
                        i == 0 ? task.getStatus() : "",
                        i == 0 ? task.getCreatedAt() : "",
                        i == 0 ? (task.getUpdatedAt() == null ? "" : task.getUpdatedAt()) : ""
                );
            }
        }
    }

    public void listTasks(String status) {
        List<Task> tasks = repository.getTasksByStatus(status);

        if (tasks.isEmpty()) {
            System.out.println("No tasks found with status: " + status);
            return;
        }

        // Print the header with equal spacing
        System.out.printf("%-" + idWidth + "s%-" + descriptionWidth + "s%-" + statusWidth + "s%-" + createdAtWidth + "s%-" + updatedAtWidth + "s\n",
                "ID", "Description", "Status", "Created At", "Updated At");

        // Print a separator line
        System.out.println("-".repeat(idWidth + descriptionWidth + statusWidth + createdAtWidth + updatedAtWidth));

        // Print the task data rows with equal column spacing
        for (Task task : tasks) {
            // Wrap the description if it's too long in new lines
            String description = task.getDescription();
            for (int i = 0; i < description.length(); i += descriptionWidth - 3) {
                System.out.printf("%-" + idWidth + "s%-" + descriptionWidth + "s%-" + statusWidth + "s%-" + createdAtWidth + "s%-" + updatedAtWidth + "s\n",
                        i == 0 ? task.getId() : "",
                        description.substring(i, Math.min(i + descriptionWidth - 3, description.length())),
                        i == 0 ? task.getStatus() : "",
                        i == 0 ? task.getCreatedAt() : "",
                        i == 0 ? (task.getUpdatedAt() == null ? "" : task.getUpdatedAt()) : ""
                );
            }
        }
    }
}
