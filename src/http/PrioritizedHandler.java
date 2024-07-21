package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import managers.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.util.List;

public class PrioritizedHandler extends BaseHttpHandler {
    public PrioritizedHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) {
        try {
            String httpMethod = exchange.getRequestMethod();
            switch (HttpMethods.valueOf(httpMethod)) {
                case GET -> handleGetPrioritized(exchange);
                default -> sendNotFound(exchange);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleGetPrioritized(HttpExchange exchange) throws IOException {
        try {
            List<Task> prioritizedList = getTaskManager().getPrioritizedTasks();
            sendText(prioritizedList, exchange, 200);
        } catch (Exception e) {
            e.printStackTrace();
            sendHasCode500(exchange);
        }
    }
}
