package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.InMemoryTaskManager;
import managers.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

abstract public class BaseHttpHandler implements HttpHandler {

    private final InMemoryTaskManager taskManager;
    private final Gson gson;

    public TaskManager getTaskManager() {
        return taskManager;
    }

    public Gson getGson() {
        return gson;
    }

    protected BaseHttpHandler(InMemoryTaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    abstract public void handle(HttpExchange exchange);

    int notFoundCode = 404;
    int servesErrorCode = 500;
    int internalCode = 406;
    String notFoundText = "Not Found";
    String internalText = "Internal Server Error";
    String serverErrorText = "Not Acceptable";

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

    protected void sendHasInteractions(HttpExchange exchange) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse(internalText);
        sendText(errorResponse, exchange, internalCode);
    }

    protected void sendHasCode500(HttpExchange exchange) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse(serverErrorText);
        sendText(errorResponse, exchange, servesErrorCode);
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
