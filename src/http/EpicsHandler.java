package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import managers.InMemoryTaskManager;
import tasks.Epic;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

public class EpicsHandler extends BaseHttpHandler {

    protected EpicsHandler(InMemoryTaskManager taskManager, Gson gson) {
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
        if (idFromPath.isPresent()) {
            Epic epic = getTaskManager().getEpicById(idFromPath.get());
            sendText(epic, exchange, 200);
        }
        if (idFromPath.isEmpty()) {
            Optional<List<Epic>> optionalEpicList = Optional.ofNullable(getTaskManager().getAllEpics());
            if (optionalEpicList.isPresent()) {
                List<Epic> epicList = optionalEpicList.get();
                sendText(epicList, exchange, 200);
            }
            if (optionalEpicList.isEmpty()) {
                sendNotFound(exchange);
            }
        }
    }

    public void handlePostEpic(HttpExchange exchange) throws IOException {
        try {
            String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Epic epic = getGson().fromJson(requestBody, Epic.class);
            Optional<Integer> idFromPath = getIdFromPath(exchange);
            if (idFromPath.isPresent()) {
                getTaskManager().updateEpic(epic);
                sendText(epic, exchange, 200);
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
                sendNotFound(exchange);
            }
            if (idFromPath.isPresent()) {
                Epic epicToRemove = getTaskManager().getEpicById(idFromPath.get());
                getTaskManager().removeEpicById(idFromPath.get());
                sendText(epicToRemove, exchange, 200);
            }
        } catch (Exception e) {
            sendHasCode500(exchange);
        }
    }
}
