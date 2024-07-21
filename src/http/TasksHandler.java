package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import managers.InMemoryTaskManager;
import tasks.Task;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

public class TasksHandler extends BaseHttpHandler {


    protected TasksHandler(InMemoryTaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) {
        try {
            String httpMethod = exchange.getRequestMethod();
            switch (HttpMethods.valueOf(httpMethod)) {
                case GET -> handleGetTask(exchange);
                case POST -> handlePostTask(exchange);
                case DELETE -> handleDeleteTaskById(exchange);
                default -> sendNotFound(exchange);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleGetTask(HttpExchange exchange) throws IOException {
        Optional<Integer> idFromPath = getIdFromPath(exchange);
        if (idFromPath.isPresent()) {
            Task task = getTaskManager().getTaskById(idFromPath.get());
            sendText(task, exchange, 200);
        }
        if (idFromPath.isEmpty()) {
            Optional<List<Task>> optionalTaskList = Optional.ofNullable(getTaskManager().getAllTasks());
            if (optionalTaskList.isPresent()) {
                List<Task> taskList = optionalTaskList.get();
                sendText(taskList, exchange, 200);
            }
            if (optionalTaskList.isEmpty()) {
                sendNotFound(exchange);
            }
        }
    }

    public void handlePostTask(HttpExchange exchange) throws IOException {
        try {
            String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Task task = getGson().fromJson(requestBody, Task.class);
            Optional<Integer> idFromPath = getIdFromPath(exchange);
            if (idFromPath.isPresent()) {
                getTaskManager().updateTask(task);
                sendText(task, exchange, 200);
            }

            if (idFromPath.isEmpty()) {
                if (!getTaskManager().isIntersectsExistingTask(task)) {
                    getTaskManager().createTask(task);
                    sendText(task, exchange, 201);
                } else {
                    sendHasInteractions(exchange);
                }
            }
        } catch (Exception e) {
            sendHasCode500(exchange);
        }
    }

    public void handleDeleteTaskById(HttpExchange exchange) throws IOException {
        try {
            Optional<Integer> idFromPath = getIdFromPath(exchange);
            if (idFromPath.isEmpty()) {
                sendNotFound(exchange);
            }
            if (idFromPath.isPresent()) {
                if (!getTaskManager().containsId(idFromPath.get())) {
                    sendNotFound(exchange);
                    return;
                }
                Task taskToRemove = getTaskManager().getTaskById(idFromPath.get());
                getTaskManager().removeTaskById(idFromPath.get());
                sendText(taskToRemove, exchange, 200);
            }
        } catch (Exception e) {
            sendHasCode500(exchange);
        }
    }
}



