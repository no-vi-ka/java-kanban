package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import managers.InMemoryTaskManager;
import tasks.SubTask;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

public class SubTasksHandler extends BaseHttpHandler {

    protected SubTasksHandler(InMemoryTaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) {
        try {
            String httpMethod = exchange.getRequestMethod();
            switch (HttpMethods.valueOf(httpMethod)) {
                case GET -> handleGetSubTask(exchange);
                case POST -> handlePostSubTask(exchange);
                case DELETE -> handleDeleteSubTaskById(exchange);
                default -> sendNotFound(exchange);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleGetSubTask(HttpExchange exchange) throws IOException {
        Optional<Integer> idFromPath = getIdFromPath(exchange);
        if (idFromPath.isPresent()) {
            SubTask subTask = getTaskManager().getSubTaskById(idFromPath.get());
            sendText(subTask, exchange, 200);
        }
        if (idFromPath.isEmpty()) {
            Optional<List<SubTask>> optionalSubTaskList = Optional.ofNullable(getTaskManager().getAllSubTasks());
            if (optionalSubTaskList.isPresent()) {
                List<SubTask> subTaskList = optionalSubTaskList.get();
                sendText(subTaskList, exchange, 200);
            }
            if (optionalSubTaskList.isEmpty()) {
                sendNotFound(exchange);
            }
        }
    }

    public void handlePostSubTask(HttpExchange exchange) throws IOException {
        try {
            String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            SubTask subTask = getGson().fromJson(requestBody, SubTask.class);
            Optional<Integer> idFromPath = getIdFromPath(exchange);
            if (idFromPath.isPresent()) {
                getTaskManager().updateSubTask(subTask);
                sendText(subTask, exchange, 200);
            }

            if (idFromPath.isEmpty()) {
                getTaskManager().createSubTask(subTask);
                sendText(subTask, exchange, 201);
            }
        } catch (Exception e) {
            sendHasInteractions(exchange);
        }
    }

    public void handleDeleteSubTaskById(HttpExchange exchange) throws IOException {
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
                SubTask taskToRemove = getTaskManager().getSubTaskById(idFromPath.get());
                getTaskManager().removeSubtaskById(idFromPath.get());
                sendText(taskToRemove, exchange, 200);
            }
        } catch (Exception e) {
            sendHasCode500(exchange);
        }
    }
}

