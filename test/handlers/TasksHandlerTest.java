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
import tasks.Task;

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

class TasksHandlerTest extends BaseHttpHandlerTest {
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
    public void testAddTask() throws IOException, InterruptedException {

        Task task1 = new Task("a1", "b1", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                10, 10, 10));

        String taskJson = gson.toJson(task1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        List<Task> tasksFromManager = manager.getAllTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("a1", tasksFromManager.get(0).getTitle(), "Некорректное имя задачи");
    }


    @Test
    void shouldNotAddIfIntersectsWithExisting() throws IOException, InterruptedException {
        Task task1 = new Task("a1", "b1", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                10, 10, 10));

        assertDoesNotThrow(() -> {
            Task taskClone = new Task(task1);
            manager.createTask(taskClone);
        });

        String taskJson = gson.toJson(task1);

        URI url = URI.create("http://localhost:8080/tasks");
        HttpResponse<String> response = sendHttpRequest(url, "POST", taskJson);

        assertEquals(406, response.statusCode());
    }


    @Test
    public void shouldReturnListFromManager() throws IOException, InterruptedException {
        Task task1 = new Task("a1", "b1", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                10, 10, 10));
        Task task2 = new Task("a2", "b2", Duration.ofMinutes(20), LocalDateTime.of(2020, Month.JULY,
                20, 20, 20));

        assertDoesNotThrow(() -> {
            manager.createTask(task1);
            manager.createTask(task2);
        });


        URI url = URI.create("http://localhost:8080/tasks");
        HttpResponse<String> response = sendHttpRequest(url, "GET", "");

        assertEquals(200, response.statusCode());

        List<Task> tasksFromRequest = gson.fromJson(response.body(), new TaskListTypeToken().getType());

        assertArrayEquals(manager.getAllTasks().toArray(), tasksFromRequest.toArray(), "Списки задач не совпадают");
    }


    @Test
    public void shouldGetById() throws IOException, InterruptedException {
        Task task1 = new Task("a1", "b1", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                10, 10, 10));

        assertDoesNotThrow(() -> {
            manager.createTask(task1);
        });

        String taskJson = gson.toJson(task1);

        URI url = URI.create(String.format("http://localhost:8080/tasks/%d", task1.getId()));
        HttpResponse<String> response = sendHttpRequest(url, "GET", "");

        assertEquals(200, response.statusCode());

        Task taskFromRequest = gson.fromJson(response.body(), Task.class);

        assertEquals(task1, taskFromRequest, "Задачи не совпадают");
    }


    @Test
    public void shouldDeleteById() throws IOException, InterruptedException {
        Task task1 = new Task("a1", "b1", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                10, 10, 10));

        assertDoesNotThrow(() -> {
            manager.createTask(task1);
        });

        URI url = URI.create(String.format("http://localhost:8080/tasks/%d", 1));
        HttpResponse<String> response = sendHttpRequest(url, "DELETE", "");

        assertEquals(200, response.statusCode());

        List<Task> tasks = manager.getAllTasks();

        assertEquals(tasks.size(), 0, "Задачи не удалена");
    }
}

class TaskListTypeToken extends TypeToken<List<Task>> {
}

