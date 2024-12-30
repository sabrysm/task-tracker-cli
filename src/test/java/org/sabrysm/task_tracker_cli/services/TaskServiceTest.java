package org.sabrysm.task_tracker_cli.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sabrysm.task_tracker_cli.models.Task;
import org.sabrysm.task_tracker_cli.repositories.TaskRepository;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @TempDir
    Path tempDir;

    @Mock
    private TaskRepository repository;

    @InjectMocks
    private TaskService taskService;

    private DateTimeFormatter formatter;
    private Path testDbPath;

    @BeforeEach
    void setUp() {
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:SS");
        testDbPath = tempDir.resolve("test-tasks.json");
    }

    @AfterEach
    void tearDown() throws IOException {
        // Clean up test database file
        if (testDbPath != null && testDbPath.toFile().exists()) {
            testDbPath.toFile().delete();
        }
    }

    @Test
    void addTask_ShouldCreateNewTask() throws IOException {
        // Arrange
        int expectedId = 1;
        String description = "Test task";
        when(repository.nextId()).thenReturn(expectedId);

        // Act
        taskService.addTask(description);

        // Assert
        verify(repository).nextId();
        verify(repository).addTask(any(Task.class));
    }

    @Test
    void updateTask_WithValidId_ShouldUpdateTask() throws IOException {
        // Arrange
        int id = 1;
        String newDescription = "Updated description";
        Task existingTask = new Task(id, "Old description", "todo", 
            LocalDateTime.now().format(formatter), null);
        
        when(repository.isTaskExists(id)).thenReturn(true);
        when(repository.getTask(id)).thenReturn(existingTask);

        // Act
        taskService.updateTask(id, newDescription);

        // Assert
        verify(repository).updateTask(eq(id), eq(newDescription), eq("todo"));
    }

    @Test
    void deleteTask_WithValidId_ShouldDeleteTask() throws IOException {
        // Arrange
        int id = 1;
        when(repository.isTaskExists(id)).thenReturn(true);

        // Act
        taskService.deleteTask(id);

        // Assert
        verify(repository).deleteTask(id);
    }

    @Test
    void markTask_WithValidIdAndStatus_ShouldUpdateTaskStatus() throws IOException {
        // Arrange
        int id = 1;
        String status = "done";
        Task existingTask = new Task(id, "Test task", "todo", 
            LocalDateTime.now().format(formatter), null);
        
        when(repository.isTaskExists(id)).thenReturn(true);
        when(repository.getTask(id)).thenReturn(existingTask);

        // Act
        taskService.markTask(id, status);

        // Assert
        verify(repository).updateTask(eq(id), eq(existingTask.getDescription()), eq(status));
    }

    @Test
    void listTasks_ShouldDisplayAllTasks() throws IOException {
        // Arrange
        List<Task> tasks = Arrays.asList(
            new Task(1, "Task 1", "todo", LocalDateTime.now().format(formatter), null),
            new Task(2, "Task 2", "in-progress", LocalDateTime.now().format(formatter), null)
        );
        when(repository.getTasks()).thenReturn(tasks);

        // Act
        taskService.listTasks();

        // Assert
        verify(repository).getTasks();
    }

    @Test
    void listTasks_WithStatus_ShouldDisplayFilteredTasks() {
        // Arrange
        String status = "done";
        List<Task> tasks = Collections.singletonList(
            new Task(1, "Task 1", "done", LocalDateTime.now().format(formatter), null)
        );
        when(repository.getTasksByStatus(status)).thenReturn(tasks);

        // Act
        taskService.listTasks(status);

        // Assert
        verify(repository).getTasksByStatus(status);
    }
}
