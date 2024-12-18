package org.sabrysm.task_tracker_cli.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Task {
    private final int id;
    private String description;
    private String status;
    private final String createdAt;
    private String updatedAt;
}
