import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import models.Status;
import models.Task;

void main() {
    Scanner scanner = new Scanner(System.in);
    Scanner sc = new Scanner(System.in);

    System.out.println("Welcome to the ToDo application!");
    while (true) {
        System.out.println("""
                Select the item you want to perform:
                1 - Add task
                2 - List of tasks
                3 - Update task
                4 - Delete task
                5 - Change status
                6 - Show tasks that are not done
                7 - Show tasks that are done
                8 - Show tasks that are in progress
                9 - End the program""");
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
                    System.out.println(tasks.get(i).getStatus());
                    System.out.println(tasks.get(i).getCreatedAt());
                    System.out.println(tasks.get(i).getUpdatedAt());
                }
                break;

            case 3:
                Scanner scInt = new Scanner(System.in);
                Scanner scStr = new Scanner(System.in);
                System.out.println("Enter task's number to update:");
                int number = scInt.nextInt();
                System.out.println("Enter new description");
                String newDescription = scStr.nextLine();
                updateTask(number, newDescription);
                break;

            case 4:
                System.out.println("Enter task's number to delete:");
                int deleteId = sc.nextInt();
                System.out.println("Enter new description");
                deleteTask(deleteId);
                break;

            case 5:
                System.out.println("Enter task's number:");
                int updateStatusId = scanner.nextInt();
                System.out.println("""
                        Enter new status:
                        1 - Todo
                        2 - In progress
                        3 - Done""");
                int newStatus = scanner.nextInt();
                switch (newStatus) {
                    case 1 -> updateStatus(updateStatusId, Status.todo);
                    case 2 -> updateStatus(updateStatusId, Status.in_progress);
                    case 3 -> updateStatus(updateStatusId, Status.done);
                    default -> System.out.println("Error");
                }

            case 6:
                List<Task> notDoneTasks = getSelectedTasks(Status.todo);
                for (int i = 0; i < notDoneTasks.toArray().length; i++) {
                    System.out.println("********************************");
                    System.out.println((notDoneTasks.get(i).getNumber()) + ".");
                    System.out.println(notDoneTasks.get(i).getDescription());
                    System.out.println(notDoneTasks.get(i).getStatus());
                }
                break;

            case 7:
                List<Task> doneTasks = getSelectedTasks(Status.done);
                for (int i = 0; i < doneTasks.toArray().length; i++) {
                    System.out.println("********************************");
                    System.out.println((doneTasks.get(i).getNumber()) + ".");
                    System.out.println(doneTasks.get(i).getDescription());
                    System.out.println(doneTasks.get(i).getStatus());
                }
                break;

            case 8:
                List<Task> inProgressTasks = getSelectedTasks(Status.in_progress);
                for (int i = 0; i < inProgressTasks.toArray().length; i++) {
                    System.out.println("********************************");
                    System.out.println((inProgressTasks.get(i).getNumber()) + ".");
                    System.out.println(inProgressTasks.get(i).getDescription());
                    System.out.println(inProgressTasks.get(i).getStatus());
                }
                break;

            case 9:
                return;

        }
    }

}

void addTask() {
    Scanner sc = new Scanner(System.in);
    Gson gson = buildGson();
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

        Task task = new Task(uniqueId, nextNumber, description, Status.todo, LocalDateTime.now(), LocalDateTime.now());

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
    Gson gson = buildGson();
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
    Gson gson = buildGson();
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
                task.setUpdatedAt(LocalDateTime.now());
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
    Gson gson = buildGson();
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

void updateStatus(int id, Status status) {
    Gson gson = buildGson();
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
                task.setStatus(status);
                task.setUpdatedAt(LocalDateTime.now());
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
        System.out.println("Status updated");
    } catch (IOException e) {
        throw new RuntimeException();
    }
}

List<Task> getSelectedTasks(Status status) {
    Gson gson = buildGson();
    File file = new File("tasks.json");

    if (!file.exists() || file.length() == 0) {
        return new ArrayList<>();
    }

    try (Reader reader = new FileReader(file)) {
        Type type = new TypeToken<List<Task>>() {
        }.getType();

        List<Task> allTasks = gson.fromJson(reader, type);
        List<Task> selectedTasks = new ArrayList<>();
        for (Task task : allTasks) {
            if (task.getStatus().equals(status)) {
                selectedTasks.add(task);
            }
        }

        if (selectedTasks.isEmpty()) return new ArrayList<>();
        return selectedTasks;
    } catch (IOException e) {
        throw new RuntimeException();
    }
}

Gson buildGson() {
    return new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
                @Override
                public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
                    return new JsonPrimitive(src.toString());
                }
            })
            .registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                @Override
                public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
                    return LocalDateTime.parse(json.getAsString());
                }
            })
            .create();
}