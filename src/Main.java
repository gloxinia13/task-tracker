import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import models.Status;
import models.Task;

void main() {
    Scanner scanner = new Scanner(System.in);

    System.out.println("Welcome to the ToDo application!");
    System.out.println("""
            Select the item you want to perform:
            1 - Add task
            2 - List of tasks""");
    int item = scanner.nextInt();
    switch (item) {
        case 1:
            addTask();
            break;
        case 2:
            List<Task> tasks = getAllTasks();
            for(Task t : tasks) {
                System.out.println("********************************");
                System.out.println(t.getDescription());
                System.out.println("Status: " + t.getStatus());
            }
            break;
    }

}

void addTask() {
    Scanner sc = new Scanner(System.in);
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    String uniqueId = UUID.randomUUID().toString();

    System.out.println("Enter task description:");
    String description = sc.nextLine();
    Task task = new Task(uniqueId, description, Status.todo);
    File file = new File("tasks.json");

    try {
        List<Task> tasks = new ArrayList<>();

        if (file.exists() && file.length() > 0) {
            Reader reader = new FileReader(file);
            Type type = new TypeToken<List<Task>>() {
            }.getType();
            tasks = gson.fromJson(reader, type);
            reader.close();
        }

        tasks.add(task);

        Writer writer = new FileWriter(file);
        gson.toJson(tasks, writer);
        writer.close();
        System.out.println("The task was successfully added");
    } catch (IOException e) {
        throw new RuntimeException();
    }
}

List<Task> getAllTasks() {
    Gson gson = new Gson();
    File file = new File("tasks.json");

    if (!file.exists() || file.length() == 0) {
        return new ArrayList<>();
    }

    try (Reader reader = new FileReader(file)) {
        Type type = new TypeToken<List<Task>>() {
        }.getType();
        return gson.fromJson(reader, type);
    } catch (IOException e) {
        throw new RuntimeException();
    }
}