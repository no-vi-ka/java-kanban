package handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import http.DurationAdapter;
import http.HttpTaskServer;
import http.LocalDateAdapter;
import managers.InMemoryTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

abstract class BaseHttpHandlerTest {
    InMemoryTaskManager manager;
    HttpTaskServer server;
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();


    @BeforeEach
    public void setUp() {
        manager = new InMemoryTaskManager();
        manager.removeAllTasks();
        manager.removeAllSubTasks();
        manager.removeAllEpics();
        server = new HttpTaskServer(8080, manager);
        server.start();
    }

    @AfterEach
    public void shutDown() {
        server.stop();
    }

    HttpResponse<String> sendHttpRequest(URI url, String method, String body) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(url).method(method, HttpRequest.BodyPublishers.ofString(body)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }
}

