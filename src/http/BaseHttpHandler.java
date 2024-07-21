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

    final int notFoundCode = 404;
    final int badRequestCode = 400;
    final int serverErrorCode = 500;
    final int internalCode = 406;
    final String notFoundText = "Not Found";
    final String badRequestText = "Bad Request";
    final String internalText = "Internal Server Error";
    final String serverErrorText = "Not Acceptable";

    protected void sendText(Object body, HttpExchange exchange, int code) throws IOException {
        String responseJson = gson.toJson(body);
        byte[] responseBytes = responseJson.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(code, responseBytes.length);
        exchange.getResponseBody().write(responseBytes);
        exchange.close();
    }

    protected void sendNotFound(HttpExchange exchange) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse(notFoundText);
        sendText(errorResponse, exchange, notFoundCode);
    }

    protected void sendBadRequest(HttpExchange exchange) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse(badRequestText);
        sendText(errorResponse, exchange, badRequestCode);
    }

    protected void sendHasInteractions(HttpExchange exchange) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse(internalText);
        sendText(errorResponse, exchange, internalCode);
    }

    protected void sendHasCode500(HttpExchange exchange) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse(serverErrorText);
        sendText(errorResponse, exchange, serverErrorCode);
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
