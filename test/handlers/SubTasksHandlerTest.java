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
import tasks.SubTask;

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

class SubTasksHandlerTest extends BaseHttpHandlerTest {
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
    public void testAddSubTask() throws IOException, InterruptedException {
        Epic epic = new Epic("a1", "b1", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                10, 10, 10));
        assertDoesNotThrow(() -> {
            manager.createEpic(epic);
        });
        SubTask subTask = new SubTask("a11", "b1", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                10, 10, 10), epic.getId());

        String taskJson = gson.toJson(subTask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        List<SubTask> subTasksFromManager = manager.getAllSubTasks();

        assertNotNull(subTasksFromManager, "Задачи не возвращаются");
        assertEquals(1, subTasksFromManager.size(), "Некорректное количество задач");
        assertEquals("a11", subTasksFromManager.get(0).getTitle(), "Некорректное имя задачи");
    }

    @Test
    public void shouldReturnListFromManager() throws IOException, InterruptedException {
        Epic epic = new Epic("a1", "b1", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                10, 10, 10));
        assertDoesNotThrow(() -> {
            manager.createEpic(epic);
        });
        SubTask subTask = new SubTask("a1", "b1", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                10, 10, 10), epic.getId());
        SubTask subTask2 = new SubTask("a2", "b2", Duration.ofMinutes(20), LocalDateTime.of(2020, Month.JULY,
                20, 20, 20), epic.getId());

        assertDoesNotThrow(() -> {
            manager.createSubTask(subTask);
            manager.createSubTask(subTask2);
        });


        URI url = URI.create("http://localhost:8080/subtasks");
        HttpResponse<String> response = sendHttpRequest(url, "GET", "");

        assertEquals(200, response.statusCode());

        List<SubTask> subTasksFromRequest = gson.fromJson(response.body(), new SubTaskListTypeToken().getType());

        assertArrayEquals(manager.getAllSubTasks().toArray(), subTasksFromRequest.toArray(), "Списки подзадач не совпадают");
    }

    @Test
    public void shouldGetById() throws IOException, InterruptedException {
        Epic epic = new Epic("a1", "b1", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                10, 10, 10));
        assertDoesNotThrow(() -> {
            manager.createEpic(epic);
        });
        SubTask subTask = new SubTask("a1", "b1", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                10, 10, 10), epic.getId());


        assertDoesNotThrow(() -> {
            manager.createSubTask(subTask);
        });

        URI url = URI.create(String.format("http://localhost:8080/subtasks/%d", subTask.getId()));
        HttpResponse<String> response = sendHttpRequest(url, "GET", "");

        assertEquals(200, response.statusCode());

        SubTask subTaskFromRequest = gson.fromJson(response.body(), SubTask.class);

        assertEquals(subTask, subTaskFromRequest, "Подзадачи не совпадают");
    }

    @Test
    public void shouldDeleteById() throws IOException, InterruptedException {
        Epic epic = new Epic("a1", "b1", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                10, 10, 10));
        assertDoesNotThrow(() -> {
            manager.createEpic(epic);
        });
        SubTask subTask = new SubTask("a1", "b1", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                10, 10, 10), epic.getId());


        assertDoesNotThrow(() -> {
            manager.createSubTask(subTask);
        });

        URI url = URI.create(String.format("http://localhost:8080/subtasks/%d", subTask.getId()));
        HttpResponse<String> response = sendHttpRequest(url, "DELETE", "");

        assertEquals(200, response.statusCode());

        List<SubTask> subTasks = manager.getAllSubTasks();

        assertEquals(subTasks.size(), 0, "Подзадача не удалена");
    }
}

class SubTaskListTypeToken extends TypeToken<List<SubTask>> {
}

