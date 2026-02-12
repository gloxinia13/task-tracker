package models;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }
}
