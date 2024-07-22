package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import managers.InMemoryTaskManager;
import managers.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {

    private static int port = 8080;
    private static HttpServer httpServer;
    private static TaskManager taskManager = new InMemoryTaskManager();
    private final Gson gson;

    public HttpTaskServer(int port, TaskManager taskManager) {
        this.port = port;
        this.taskManager = taskManager;
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
    }

    public void start() {
        try {
            httpServer = HttpServer.create(new InetSocketAddress(port), 0);
            initRoutes();
            httpServer.start();
        } catch (IOException e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }

    }

    public static void stop() {
        httpServer.stop(0);
    }

    private void initRoutes() {
        httpServer.createContext("/tasks", new TasksHandler(taskManager, gson));
        httpServer.createContext("/subtasks", new SubTasksHandler(taskManager, gson));
        httpServer.createContext("/epics", new EpicsHandler(taskManager, gson));
        httpServer.createContext("/history", new HistoryHandler(taskManager, gson));
        httpServer.createContext("/tasks/prioritized", new PrioritizedHandler(taskManager, gson));
    }

    public static void main(String[] args) {
        System.out.println("Поехали!");
        HttpTaskServer httpTaskServer = new HttpTaskServer(port, taskManager);
        httpTaskServer.start();
    }
}