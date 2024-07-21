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
    public static int PORT = 8080;

    public static void main(String[] args) throws IOException {
        System.out.println("Поехали!");

        HttpTaskServer httpTaskServer = new HttpTaskServer(PORT, taskManager);
        httpTaskServer.start();
    }
}



