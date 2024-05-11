import managers.InMemoryHistoryManager;
import tasks.*;
import managers.InMemoryTaskManager;

public class Main {

    private static final InMemoryTaskManager manager = new InMemoryTaskManager();
    private static final InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

    public static void main(String[] args) {
        System.out.println("Поехали!");
        System.out.println("Создание объектов... ");

        Task task1 = manager.createTask(new Task("Задача 1.", "Описание первой задачи."));
        Task task2 = manager.createTask(new Task("Задача 2.", "Описание второй задачи."));
        Epic epic1 = manager.createEpic(new Epic("Эпик 1.", "Описание первого эпика."));
        manager.createEpic(epic1);

        SubTask subTask1 = new SubTask("Подзадача 1.", "Описание подзадачи 1.", epic1.getId());
        SubTask subTask2 = new SubTask("Подзадача 2.", "Описание подзадачи 2.", epic1.getId());
        manager.createSubTask(subTask1);
        manager.createSubTask(subTask2);

        Epic epic2 = new Epic("Эпик 2.", "Описание эпика 2.");
        manager.createEpic(epic2);

        SubTask subTask3 = new SubTask("Подзадача 3.", "Описание подзадачи 3.", epic2.getId());
        manager.createSubTask(subTask3);

        printTasks();

        System.out.println("Изменение статусов... ");

        task1.setStatus(Status.IN_PROGRESS);
        manager.updateTask(task1);
        task2.setStatus(Status.DONE);
        manager.updateTask(task2);
        subTask1.setStatus(Status.DONE);
        manager.updateSubTask(subTask1);
        subTask2.setStatus(Status.NEW);
        manager.updateSubTask(subTask2);
        subTask3.setStatus(Status.DONE);
        manager.updateSubTask(subTask3);

        printTasks();

        System.out.println("Удаление задач... ");

        manager.removeTaskById(task1.getId());
        manager.removeEpicById(epic1.getId());

        printTasks();

    }

    private static void printTasks() {
        System.out.println("Список эпиков: ");
        System.out.println(manager.getAllEpics());
        System.out.println("Список задач: ");
        System.out.println(manager.getAllTasks());
        System.out.println("Список подзадач: ");
        System.out.println(manager.getAllSubTasks());
        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}

