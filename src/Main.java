import com.google.gson.Gson;
import models.Status;
import models.Task;

void main() {
    Scanner scanner = new Scanner(System.in);

    System.out.println("Welcome to the ToDo application!");
    System.out.println("""
            Select the item you want to perform:
            1 - Add task
            2 - List of all tasks""");
    int item = scanner.nextInt();
    switch (item) {
        case 1:
            addTask();
            break;
        case 2:
            break;
    }

}

void addTask() {
    Scanner sc = new Scanner(System.in);
    Gson gson = new Gson();
    String uniqueId = UUID.randomUUID().toString();

    System.out.println("Enter task description:");
    String description = sc.nextLine();
    Task task = new Task(uniqueId, description, Status.todo);

    try (Writer writer = new FileWriter("tasks.json")) {
        gson.toJson(task, writer);
        System.out.println("The task was added");
    } catch (IOException e) {
        throw new RuntimeException();
    }
}