import http.HttpTaskServer;
import managers.InMemoryTaskManager;

public class Main {

    private static final InMemoryTaskManager taskManager = new InMemoryTaskManager();
    public static int PORT = 8080;

    public static void main(String[] args) {
        System.out.println("Поехали!");
        HttpTaskServer httpTaskServer = new HttpTaskServer(PORT, taskManager);
        httpTaskServer.start();
    }
}



