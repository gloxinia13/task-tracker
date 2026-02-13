import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import models.Status;
import models.Task;

void main() {
    Scanner scanner = new Scanner(System.in);

    System.out.println("Welcome to the ToDo application!");
    while (true) {
        System.out.println("""
                Select the item you want to perform:
                1 - Add task
                2 - List of tasks
                3 - Update task
                4 - Delete task""");
        int item = scanner.nextInt();
        switch (item) {
            case 1:
                addTask();
                break;
            case 2:
                List<Task> tasks = getAllTasks();
                for (int i = 0; i < tasks.toArray().length; i++) {
                    System.out.println("********************************");
                    System.out.println((tasks.get(i).getNumber()) + ".");
                    System.out.println(tasks.get(i).getDescription());
                }
                break;

            case 3:
                Scanner scInt = new Scanner(System.in);
                Scanner scStr = new Scanner(System.in);
                System.out.println("Enter task's number to update");
                int number = scInt.nextInt();
                System.out.println("Enter new description");
                String newDescription = scStr.nextLine();
                updateTask(number, newDescription);
                break;

            case 4:
                Scanner sc = new Scanner(System.in);
                System.out.println("Enter task's number to delete");
                int deleteId = sc.nextInt();
                System.out.println("Enter new description");
                deleteTask(deleteId);
                break;
        }
    }

}

void addTask() {
    Scanner sc = new Scanner(System.in);
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    String uniqueId = UUID.randomUUID().toString();

    System.out.println("Enter task description:");
    String description = sc.nextLine();
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

        int nextNumber = tasks.stream()
                .mapToInt(Task::getNumber).
                max()
                .orElse(0) + 1;

        Task task = new Task(uniqueId, nextNumber, description, Status.todo);

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

void updateTask(int id, String description) {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    File file = new File("tasks.json");

    if (!file.exists() || file.length() == 0) {
        System.out.println("No tasks found");
        return;
    }

    try (Reader reader = new FileReader(file)) {
        Type type = new TypeToken<List<Task>>() {
        }.getType();
        List<Task> tasks = gson.fromJson(reader, type);

        boolean isFound = false;

        for (Task task : tasks) {
            if (task.getNumber() == id) {
                task.setDescription(description);
                isFound = true;
                break;
            }
        }

        if (!isFound) {
            System.out.println("Task not found");
            return;
        }

        try (Writer writer = new FileWriter(file)) {
            gson.toJson(tasks, writer);
        }
        System.out.println("Task updated");
    } catch (IOException e) {
        throw new RuntimeException();
    }
}

void deleteTask(int id) {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    File file = new File("tasks.json");

    if (!file.exists() || file.length() == 0) {
        System.out.println("No tasks found");
        return;
    }

    try (Reader reader = new FileReader(file)) {
        Type type = new TypeToken<List<Task>>() {
        }.getType();
        List<Task> tasks = gson.fromJson(reader, type);

        boolean isFound = false;

        for (Task task : tasks) {
            if (task.getNumber() == id) {
                tasks.remove(id - 1);
                isFound = true;
                break;
            }
        }

        if (!isFound) {
            System.out.println("Task not found");
            return;
        }

        try (Writer writer = new FileWriter(file)) {
            gson.toJson(tasks, writer);
        }
        System.out.println("Task deleted");
    } catch (IOException e) {
        throw new RuntimeException();
    }
}