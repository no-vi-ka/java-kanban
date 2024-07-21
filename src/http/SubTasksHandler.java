package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import managers.TaskManager;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

public class SubTasksHandler extends BaseHttpHandler {

    public SubTasksHandler(TaskManager taskManager, Gson gson) {
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
        if (idFromPath.isPresent() && getTaskManager().containsSubTaskId(idFromPath.get())) {
            SubTask subTaskById = getTaskManager().getSubTaskById(idFromPath.get());
            sendText(subTaskById, exchange, 200);
        }
        if (idFromPath.isPresent() && !getTaskManager().containsSubTaskId(idFromPath.get())) {
            sendNotFound(exchange);
        }
        if (idFromPath.isEmpty()) {
            List<SubTask> subTaskList = getTaskManager().getAllSubTasks();
            sendText(subTaskList, exchange, 200);
        }
    }

    public void handlePostSubTask(HttpExchange exchange) throws IOException {
        try {
            String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Optional<SubTask> optionalSubTask = Optional.ofNullable(getGson().fromJson(requestBody, SubTask.class));
            if (optionalSubTask.isEmpty()) {
                sendBadRequest(exchange);
                return;
            }
            SubTask subTask = optionalSubTask.get();
            Optional<Integer> idFromPath = getIdFromPath(exchange);
            if (idFromPath.isPresent() && getTaskManager().containsSubTaskId(idFromPath.get())) {
                getTaskManager().updateSubTask(subTask);
                sendText(subTask, exchange, 200);
            }
            if (idFromPath.isPresent() && !getTaskManager().containsSubTaskId(idFromPath.get())) {
                sendNotFound(exchange);
            }
            if (idFromPath.isEmpty()) {
                getTaskManager().createSubTask(subTask);
                sendText(subTask, exchange, 201);
            }
        } catch (Exception e) {
            sendHasCode500(exchange);
        }
    }

    public void handleDeleteSubTaskById(HttpExchange exchange) throws IOException {
        try {
            Optional<Integer> idFromPath = getIdFromPath(exchange);
            if (idFromPath.isEmpty()) {
                sendBadRequest(exchange);
            }
            if (idFromPath.isPresent() && getTaskManager().containsSubTaskId(idFromPath.get())) {
                SubTask subTaskToRemove = getTaskManager().getSubTaskById(idFromPath.get());
                getTaskManager().removeSubtaskById(idFromPath.get());
                sendText(subTaskToRemove, exchange, 200);
                if (idFromPath.isPresent() && !getTaskManager().containsSubTaskId(idFromPath.get())) {
                    sendNotFound(exchange);
                }
            }
        } catch (Exception e) {
            sendHasCode500(exchange);
        }
    }
}

