package org.sabrysm.task_tracker_cli;

import org.sabrysm.task_tracker_cli.services.TaskService;

import com.google.gson.Gson;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: task-cli <command> [options]");
            return;
        }

        String command = args[0].toLowerCase();
        TaskService service = new TaskService();

        try {
            switch (command) {
                case "add":
                    if (args.length < 2) {
                        System.out.println("Usage: task-cli add <description>");
                        return;
                    }
                    service.addTask(args[1]);
                    break;
                case "update":
                    if (args.length < 3) {
                        System.out.println("Usage: task-cli update <id> <description>");
                        return;
                    }
                    service.updateTask(Integer.parseInt(args[1]), args[2]);
                    break;
                case "delete":
                    if (args.length < 2) {
                        System.out.println("Usage: task-cli delete <id>");
                        return;
                    }
                    service.deleteTask(Integer.parseInt(args[1]));
                    break;
                case "mark-in-progress":
                    if (args.length < 2) {
                        System.out.println("Usage: task-cli mark-in-progress <id>");
                        return;
                    }
                    service.markTask(Integer.parseInt(args[1]), "in-progress");
                    break;
                case "mark-done":
                    if (args.length < 2) {
                        System.out.println("Usage: task-cli mark-done <id>");
                        return;
                    }
                    service.markTask(Integer.parseInt(args[1]), "done");
                    break;
                case "list":
                    if (args.length > 2) {
                        System.out.println("Usage: task-cli list [status]");
                        return;
                    } else if (args.length == 2) {
                        service.listTasks(args[1]);
                        break;
                    }
                    service.listTasks();
                    break;
                default:
                    System.out.println("Unknown command: " + command);
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}
