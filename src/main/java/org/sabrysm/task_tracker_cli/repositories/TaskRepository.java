package org.sabrysm.task_tracker_cli.repositories;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.sabrysm.task_tracker_cli.models.Task;

import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TaskRepository {
    @Getter
    private List<Task> tasks = new ArrayList<>();
    private final Gson gson = new Gson();
    private final String FILE_NAME = "E:\\tasks.json";

    public TaskRepository() {
        try {
            loadTasks();
        } catch (IOException e) {
            System.err.println("An error occurred while loading tasks: " + e.getMessage());
        }
    }

    public void loadTasks() throws IOException {
        Path path = Paths.get(FILE_NAME);

        if (!Files.exists(path)) {
            Files.createFile(path);
            Files.writeString(path, "{\"tasks\":[]}");  // Initialize with an empty "tasks" array
        }

        String content = Files.readString(path);

        // Parse the JSON content
        // 1. Create a map from the JSON content
        Map map = gson.fromJson(content, Map.class);
        // 2. Get the "tasks" key from the map
        List<Map<String, Object>> taskList = (List<Map<String, Object>>) map.get("tasks");
        // 3. Iterate over the taskList and create Task objects
        for (Map<String, Object> taskMap : taskList) {
            Task task = gson.fromJson(gson.toJson(taskMap), Task.class);
            tasks.add(task);
        }
    }

    public List<Task> getTasksByStatus(String status) {
        List<Task> tasksByStatus = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getStatus().equals(status)) {
                tasksByStatus.add(task);
            }
        }
        return tasksByStatus;
    }

    private void saveTasks() throws IOException {
        Path path = Paths.get(FILE_NAME);
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
        String content = new Gson().toJson(Collections.singletonMap("tasks", tasks));

        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write(content);
        }
    }


    public int nextId() throws IOException {
        // Read
        return tasks.size() + 1;
    }

    public Task getTask(int id) {
        for (Task task : tasks) {
            if (task.getId() == id) {
                return task;
            }
        }
        return null;
    }

    public void addTask(Task newTask) throws IOException {
        tasks.add(newTask);
        saveTasks();
    }

    public void updateTask(int id, String newDescription, String status) throws IOException {
        for (Task task : tasks) {
            if (task.getId() == id) {
                task.setDescription(newDescription);
                task.setStatus(status);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:SS");
                task.setUpdatedAt(LocalDateTime.now().format(formatter));
                saveTasks();
                return;
            }
        }
    }

    public void deleteTask(int id) throws IOException {
        for (Task task : tasks) {
            if (task.getId() == id) {
                tasks.remove(task);
                saveTasks();
                return;
            }
        }
    }

    public boolean isTaskExists(int id) {
        for (Task task : tasks) {
            if (task.getId() == id) {
                return true;
            }
        }
        return false;
    }
}
