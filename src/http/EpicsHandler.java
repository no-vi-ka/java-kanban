package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import managers.TaskManager;
import tasks.Epic;
import tasks.SubTask;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

public class EpicsHandler extends BaseHttpHandler {

    public EpicsHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) {
        try {
            String httpMethod = exchange.getRequestMethod();
            switch (HttpMethods.valueOf(httpMethod)) {
                case GET -> handleGetEpic(exchange);
                case POST -> handlePostEpic(exchange);
                case DELETE -> handleDeleteEpicById(exchange);
                default -> sendNotFound(exchange);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleGetEpic(HttpExchange exchange) throws IOException {
        Optional<Integer> idFromPath = getIdFromPath(exchange);
        String[] splittedPath = exchange.getRequestURI().getPath().split("/");
        if (idFromPath.isPresent() && getTaskManager().containsEpicId(idFromPath.get())) {
            Epic epicById = getTaskManager().getEpicById(idFromPath.get());
            if (splittedPath.length == 4) {
                List<SubTask> epicSubTaskList = getTaskManager().getAllSubtasksOfEpic(epicById);
                sendText(epicSubTaskList, exchange, 200);
                return;
            }
            sendText(epicById, exchange, 200);
        }
        if (idFromPath.isPresent() && !getTaskManager().containsEpicId(idFromPath.get())) {
            sendNotFound(exchange);
        }
        if (idFromPath.isEmpty()) {
            List<Epic> epicList = getTaskManager().getAllEpics();
            sendText(epicList, exchange, 200);
        }
    }

    public void handlePostEpic(HttpExchange exchange) throws IOException {
        try {
            String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Optional<Epic> optionalEpic = Optional.ofNullable(getGson().fromJson(requestBody, Epic.class));
            if (optionalEpic.isEmpty()) {
                sendBadRequest(exchange);
                return;
            }
            Epic epic = optionalEpic.get();
            Optional<Integer> idFromPath = getIdFromPath(exchange);
            if (idFromPath.isPresent() && getTaskManager().containsEpicId(idFromPath.get())) {
                getTaskManager().updateEpic(epic);
                sendText(epic, exchange, 200);
            }
            if (idFromPath.isPresent() && !getTaskManager().containsEpicId(idFromPath.get())) {
                sendNotFound(exchange);
            }
            if (idFromPath.isEmpty()) {
                getTaskManager().createEpic(epic);
                sendText(epic, exchange, 201);
            }
        } catch (Exception e) {
            sendHasCode500(exchange);
        }
    }

    public void handleDeleteEpicById(HttpExchange exchange) throws IOException {
        try {
            Optional<Integer> idFromPath = getIdFromPath(exchange);
            if (idFromPath.isEmpty()) {
                sendBadRequest(exchange);
            }
            if (idFromPath.isPresent()) {
                Optional<Epic> optionalEpic = Optional.ofNullable(getTaskManager().getEpicById(idFromPath.get()));
                if (optionalEpic.isEmpty()) {
                    sendNotFound(exchange);
                } else {
                    Epic epicToRemove = getTaskManager().getEpicById(idFromPath.get());
                    getTaskManager().removeEpicById(idFromPath.get());
                    sendText(epicToRemove, exchange, 200);
                }
            }
        } catch (Exception e) {
            sendHasCode500(exchange);
        }
    }
}
