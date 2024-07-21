package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import managers.InMemoryTaskManager;
import tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class PrioritizedHandler extends BaseHttpHandler {
    protected PrioritizedHandler(InMemoryTaskManager taskManager, Gson gson) {
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

    public void handleGetPrioritized(HttpExchange exchange) throws IOException {
        try {
            Optional<List<Task>> prioritizedList = Optional.ofNullable(getTaskManager().getPrioritizedTasks());
            if (prioritizedList.isPresent()) {
                sendText(prioritizedList.get(), exchange, 200);
            } else {
                sendNotFound(exchange);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendHasCode500(exchange);
        }
    }
}
