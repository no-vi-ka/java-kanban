package handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import http.DurationAdapter;
import http.HttpTaskServer;
import http.LocalDateAdapter;
import managers.InMemoryTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpicsHandlerTest extends BaseHttpHandlerTest {
    InMemoryTaskManager manager = new InMemoryTaskManager();
    public static int PORT = 8080;
    HttpTaskServer taskServer;
    Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter()).registerTypeAdapter(Duration.class, new DurationAdapter()).create();


    @BeforeEach
    public void setUp() {
        manager.removeAllTasks();
        manager.removeAllSubTasks();
        manager.removeAllEpics();
        HttpTaskServer taskServer = new HttpTaskServer(PORT, manager);
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void shouldReturnListFromManager() throws IOException, InterruptedException {
        Epic epic1 = new Epic("a1", "b1", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                10, 10, 10));
        Epic epic2 = new Epic("a2", "b2", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                10, 11, 10));

        assertDoesNotThrow(() -> {
            manager.createEpic(epic1);
            manager.createEpic(epic2);
        });


        URI url = URI.create("http://localhost:8080/epics");
        HttpResponse<String> response = sendHttpRequest(url, "GET", "");

        assertEquals(200, response.statusCode());

        List<Epic> epicsFromRequest = gson.fromJson(response.body(), new EpicListTypeToken().getType());

        assertArrayEquals(manager.getAllEpics().toArray(), epicsFromRequest.toArray(), "Списки эпиков не совпадают");
    }

    @Test
    public void shouldGetById() throws IOException, InterruptedException {
        Epic epic1 = new Epic("a1", "b1", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                10, 10, 10));

        assertDoesNotThrow(() -> {
            manager.createEpic(epic1);
        });


        URI url = URI.create(String.format("http://localhost:8080/epics/%d", epic1.getId()));
        HttpResponse<String> response = sendHttpRequest(url, "GET", "");

        assertEquals(200, response.statusCode());

        Epic epicFromRequest = gson.fromJson(response.body(), Epic.class);

        assertEquals(epic1, epicFromRequest, "Эпики не совпадают");
    }


    @Test
    public void shouldCreateNewEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("a1", "b1", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                10, 10, 10));
        String eipcJson = gson.toJson(epic);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(eipcJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        List<Epic> epicsFromManager = manager.getAllEpics();

        assertNotNull(epicsFromManager, "Задачи не возвращаются");
        assertEquals(1, epicsFromManager.size(), "Некорректное количество задач");
        assertEquals("a1", epicsFromManager.get(0).getTitle(), "Некорректное имя задачи");
    }

    @Test
    public void ShouldDeleteById() throws IOException, InterruptedException {
        Epic epic = new Epic("a1", "b1", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                10, 10, 10));

        assertDoesNotThrow(() -> {
            manager.createEpic(epic);
        });

        URI url = URI.create(String.format("http://localhost:8080/epics/%d", 1));
        HttpResponse<String> response = sendHttpRequest(url, "DELETE", "");

        assertEquals(200, response.statusCode());

        List<Epic> epics = manager.getAllEpics();

        assertEquals(epics.size(), 0, "Задачи не удалена");
    }

}

class EpicListTypeToken extends TypeToken<List<Epic>> {
}



