package models;

public class Task {
    private String id;
    private int number ;
    private String description;
    private Status status;
//    public LocalDate createdAt;
//    public LocalDate updatedAt;

    public Task(String id, int number, String description, Status status) {
        this.id = id;
        this.number = number;
        this.description = description;
        this.status = status;
    }

    public int getNumber() {
        return number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }
}
