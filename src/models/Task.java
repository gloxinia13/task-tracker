package models;

import java.time.LocalDate;
import java.util.Date;

public class Task {
    private String id;
    private String description;
    private Status status;
//    public LocalDate createdAt;
//    public LocalDate updatedAt;

    public Task(String id, String description, Status status) {
        this.id = id;
        this.description = description;
        this.status = status;
    }
}
