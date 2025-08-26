package com.bhavani.model;

import ch.qos.logback.core.status.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssueDTO {

    private Long id;

    private String title;

    private String description;

    private String status;

    private Long projectId;

    private String priority;

    private LocalDateTime dueDate;

    private List<String> tags = new ArrayList<>();

    private Project project;

    private User assignee;
}
