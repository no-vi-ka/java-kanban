package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

abstract class BaseHttpHandler implements HttpHandler {

    private final TaskManager taskManager;
    private final Gson gson;

    protected TaskManager getTaskManager() {
        return taskManager;
    }

    protected Gson getGson() {
        return gson;
    }

    protected BaseHttpHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    final int NOT_FOUND_CODE = 404;
    final int BAD_REQUEST_CODE = 400;
    final int SERVER_ERROR_CODE = 500;
    final int INTERNAL_CODE = 406;
    final String NOT_FOUND_TEXT = "Not Found";
    final String BAD_REQUEST_TEXT = "Bad Request";
    final String INTERNAL_TEXT = "Internal Server Error";
    final String SERVER_ERROR_TEXT = "Not Acceptable";

    protected void sendText(Object body, HttpExchange exchange, int code) throws IOException {
        String responseJson = gson.toJson(body);
        byte[] responseBytes = responseJson.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(code, responseBytes.length);
        exchange.getResponseBody().write(responseBytes);
        exchange.close();
    }

    protected void sendNotFound(HttpExchange exchange) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse(NOT_FOUND_TEXT);
        sendText(errorResponse, exchange, NOT_FOUND_CODE);
    }

    protected void sendBadRequest(HttpExchange exchange) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse(BAD_REQUEST_TEXT);
        sendText(errorResponse, exchange, BAD_REQUEST_CODE);
    }

    protected void sendHasInteractions(HttpExchange exchange) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse(INTERNAL_TEXT);
        sendText(errorResponse, exchange, INTERNAL_CODE);
    }

    protected void sendHasCode500(HttpExchange exchange) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse(SERVER_ERROR_TEXT);
        sendText(errorResponse, exchange, SERVER_ERROR_CODE);
    }

    protected Optional<Integer> getIdFromPath(HttpExchange exchange) {
        String[] splittedPath = exchange.getRequestURI().getPath().split("/");
        try {
            if (splittedPath.length == 3) {
                return Optional.of(Integer.parseInt(splittedPath[2]));
            } else {
                return Optional.empty();
            }
        } catch (NumberFormatException exception) {
            return Optional.empty();
        }
    }
}
