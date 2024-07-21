package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import managers.InMemoryTaskManager;
import tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class HistoryHandler extends BaseHttpHandler {
    protected HistoryHandler(InMemoryTaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) {
        try {
            String httpMethod = exchange.getRequestMethod();
            switch (HttpMethods.valueOf(httpMethod)) {
                case GET -> handleGetHistory(exchange);
                default -> sendNotFound(exchange);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleGetHistory(HttpExchange exchange) throws IOException {
        try {
            Optional<List<Task>> historyList = Optional.ofNullable(getTaskManager().getHistory());
            if (historyList.isPresent()) {
                sendText(historyList.get(), exchange, 200);
            } else {
                sendNotFound(exchange);
            }
        } catch (Exception e) {
            sendHasCode500(exchange);
        }
    }
}
