package handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import http.DurationAdapter;
import http.HttpTaskServer;
import http.LocalDateAdapter;
import managers.InMemoryTaskManager;
import managers.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PrioritizedHandlerTest extends BaseHttpHandlerTest {
    TaskManager manager = new InMemoryTaskManager();
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
    public void shouldReturnPrioritized() throws IOException, InterruptedException, IOException {


        assertDoesNotThrow(() -> {
            Task task1 = manager.createTask(new Task("a1", "b1", Duration.ofMinutes(10), LocalDateTime.of(2010, Month.JUNE,
                    10, 10, 10)));
            Task task2 = manager.createTask(new Task("a2", "b2", Duration.ofMinutes(20), LocalDateTime.of(2020, Month.JULY,
                    20, 20, 20)));
            manager.createTask(task1);
            manager.createTask(task2);
//
//            manager.getTaskById(task1.getId());
//            manager.getTaskById(task2.getId());
        });


        URI url = URI.create("http://localhost:8080/tasks/prioritized");
        HttpResponse<String> response = sendHttpRequest(url, "GET", "");

        assertEquals(200, response.statusCode());

        List<Task> tasksFromRequest = gson.fromJson(response.body(), new TaskListTypeToken().getType());

        assertNotNull(tasksFromRequest, "Задачи не возвращаются");
    }

}