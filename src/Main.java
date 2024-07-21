import http.HttpTaskServer;
import managers.InMemoryHistoryManager;
import managers.InMemoryTaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import javax.sound.sampled.Port;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

public class Main {

    private static final InMemoryTaskManager taskManager = new InMemoryTaskManager();
    private static final InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
    public static int PORT = 8080;

    public static void main(String[] args) throws IOException {
        System.out.println("Поехали!");
//                Task task1 = taskManager.createTask(new Task("Задача 1.", "Описание первой задачи.",
//                Duration.ofMinutes(30), LocalDateTime.of(2024, Month.JUNE,
//                19, 10, 20)));
//        System.out.println(task1);
        HttpTaskServer httpTaskServer = new HttpTaskServer(PORT, taskManager);
        httpTaskServer.start();
    }
}
//    }
//}
//        System.out.println("Создание объектов... ");
//
//        Task task1 = manager.createTask(new Task("Задача 1.", "Описание первой задачи.",
//                Duration.ofMinutes(30), LocalDateTime.of(2024, Month.JUNE,
//                19, 10, 20)));
//        Task task2 = manager.createTask(new Task("Задача 2.", "Описание второй задачи.",
//                Duration.ofMinutes(40), LocalDateTime.of(2014, Month.MARCH,
//                30, 30, 10)));
//        Epic epic1 = manager.createEpic(new Epic("Эпик 1.", "Описание первого эпика.",
//                Duration.ofMinutes(20), LocalDateTime.of(2020, Month.MAY,
//                40, 20, 16)));
//        manager.createEpic(epic1);
//
//        SubTask subTask1 = new SubTask("Подзадача 1.", "Описание подзадачи 1.", Duration.ofMinutes(50),
//                LocalDateTime.of(2023, Month.SEPTEMBER,
//                9, 12, 7),  epic1.getId());
//        SubTask subTask2 = new SubTask("Подзадача 2.", "Описание подзадачи 2.", Duration.ofMinutes(70),
//                LocalDateTime.of(2022, Month.FEBRUARY,
//                23, 20, 24), epic1.getId());
//        manager.createSubTask(subTask1);
//        manager.createSubTask(subTask2);
//
//        Epic epic2 = new Epic("Эпик 2.", "Описание эпика 2.", Duration.ofMinutes(20),
//                LocalDateTime.of(2021, Month.JANUARY,
//                17, 17, 27));
//        manager.createEpic(epic2);
//
//        SubTask subTask3 = new SubTask("Подзадача 3.", "Описание подзадачи 3.", Duration.ofMinutes(60),
//                LocalDateTime.of(2019, Month.APRIL, 6, 15, 39), epic2.getId());
//        manager.createSubTask(subTask3);
//
//        printTasks();
//
//        System.out.println("Изменение статусов... ");
//
//        task1.setStatus(Status.IN_PROGRESS);
//        manager.updateTask(task1);
//        task2.setStatus(Status.DONE);
//        manager.updateTask(task2);
//        subTask1.setStatus(Status.DONE);
//        manager.updateSubTask(subTask1);
//        subTask2.setStatus(Status.NEW);
//        manager.updateSubTask(subTask2);
//        subTask3.setStatus(Status.DONE);
//        manager.updateSubTask(subTask3);
//
//        printTasks();
//
//        System.out.println("Удаление задач... ");
//
//        manager.removeTaskById(task1.getId());
//        manager.removeEpicById(epic1.getId());
//
//        printTasks();
//
//    }
//
//    private static void printTasks() {
//        System.out.println("Список эпиков: ");
//        System.out.println(manager.getAllEpics());
//        System.out.println("Список задач: ");
//        System.out.println(manager.getAllTasks());
//        System.out.println("Список подзадач: ");
//        System.out.println(manager.getAllSubTasks());
//        System.out.println("История:");
//        for (Task task : manager.getHistory()) {
//            System.out.println(task);
//        }
//    }


