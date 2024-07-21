package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import managers.InMemoryTaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {

    private int port = 8080;
    private static HttpServer httpServer;
    private final InMemoryTaskManager taskManager;
    private final Gson gson;

    public HttpTaskServer(int port, InMemoryTaskManager taskManager) {
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

    public Gson getGson() {
        return this.gson;
    }

    private void initRoutes() {
        httpServer.createContext("/tasks", new TasksHandler(taskManager, gson));
        httpServer.createContext("/subtasks", new SubTasksHandler(taskManager, gson));
        httpServer.createContext("/epics", new EpicsHandler(taskManager, gson));
        httpServer.createContext("/history", new HistoryHandler(taskManager, gson));
        httpServer.createContext("/tasks/prioritized", new PrioritizedHandler(taskManager, gson));
    }
}