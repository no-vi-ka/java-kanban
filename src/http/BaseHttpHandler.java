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

    final int NOTFOUNDCODE = 404;
    final int BADREQUESTCODE = 400;
    final int SERVERERRORCODE = 500;
    final int INTERNALCODE = 406;
    final String NOTFOUNDTEXT = "Not Found";
    final String BADREQUESTTEXT = "Bad Request";
    final String INTERNALTEXT = "Internal Server Error";
    final String SERVERERRORTEXT = "Not Acceptable";

    protected void sendText(Object body, HttpExchange exchange, int code) throws IOException {
        String responseJson = gson.toJson(body);
        byte[] responseBytes = responseJson.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(code, responseBytes.length);
        exchange.getResponseBody().write(responseBytes);
        exchange.close();
    }

    protected void sendNotFound(HttpExchange exchange) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse(NOTFOUNDTEXT);
        sendText(errorResponse, exchange, NOTFOUNDCODE);
    }

    protected void sendBadRequest(HttpExchange exchange) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse(BADREQUESTTEXT);
        sendText(errorResponse, exchange, BADREQUESTCODE);
    }

    protected void sendHasInteractions(HttpExchange exchange) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse(INTERNALTEXT);
        sendText(errorResponse, exchange, INTERNALCODE);
    }

    protected void sendHasCode500(HttpExchange exchange) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse(SERVERERRORTEXT);
        sendText(errorResponse, exchange, SERVERERRORCODE);
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
