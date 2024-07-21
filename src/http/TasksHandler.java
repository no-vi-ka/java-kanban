package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import managers.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

public class TasksHandler extends BaseHttpHandler {


    public TasksHandler(TaskManager taskManager, Gson gson) {
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
        if (idFromPath.isPresent() && getTaskManager().containsTaskId(idFromPath.get())) {
            Task taskById = getTaskManager().getTaskById(idFromPath.get());
            sendText(taskById, exchange, 200);
        }
        if (idFromPath.isPresent() && !getTaskManager().containsTaskId(idFromPath.get())) {
            sendNotFound(exchange);
        }
        if (idFromPath.isEmpty()) {
            List<Task> taskList = getTaskManager().getAllTasks();
            sendText(taskList, exchange, 200);
        }
    }

    public void handlePostTask(HttpExchange exchange) throws IOException {
        try {
            String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Optional<Task> optionalTask = Optional.ofNullable(getGson().fromJson(requestBody, Task.class));
            if (optionalTask.isEmpty()) {
                sendBadRequest(exchange);
                return;
            }
            Task task = optionalTask.get();
            Optional<Integer> idFromPath = getIdFromPath(exchange);
            if (idFromPath.isPresent() && getTaskManager().containsTaskId(idFromPath.get())) {
                getTaskManager().updateTask(task);
                sendText(task, exchange, 200);
            }
            if (idFromPath.isPresent() && !getTaskManager().containsTaskId(idFromPath.get())) {
                sendNotFound(exchange);
            }
            if (idFromPath.isEmpty()) {
                getTaskManager().createTask(task);
                sendText(task, exchange, 201);
            }
        } catch (Exception e) {
            sendHasCode500(exchange);
        }
    }

    public void handleDeleteTaskById(HttpExchange exchange) throws IOException {
        try {
            Optional<Integer> idFromPath = getIdFromPath(exchange);
            if (idFromPath.isEmpty()) {
                sendBadRequest(exchange);
            }
            if (idFromPath.isPresent() && getTaskManager().containsTaskId(idFromPath.get())) {
                Task taskToRemove = getTaskManager().getTaskById(idFromPath.get());
                getTaskManager().removeTaskById(idFromPath.get());
                sendText(taskToRemove, exchange, 200);
                if (idFromPath.isPresent() && !getTaskManager().containsTaskId(idFromPath.get())) {
                    sendNotFound(exchange);
                }
            }
        } catch (Exception e) {
            sendHasCode500(exchange);
        }
    }
}