import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.enums.StatusType;
import ru.yandex.practicum.serverclient.HttpTaskServer;
import ru.yandex.practicum.tasktypes.Epic;
import ru.yandex.practicum.tasktypes.Subtask;
import ru.yandex.practicum.tasktypes.Task;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskServerTest {

    HttpTaskServer httpTaskServer;
    HttpClient httpClient;
    Gson gson;
    URI tasksTaskUrl = URI.create("http://localhost:8080/tasks/task");
    URI tasksSubtaskUrl = URI.create("http://localhost:8080/tasks/subtask");
    URI tasksSubtaskEpicUrl = URI.create("http://localhost:8080/tasks/subtask/epic");
    URI tasksEpicUrl = URI.create("http://localhost:8080/tasks/epic");
    URI tasksHistoryUrl = URI.create("http://localhost:8080/tasks/history");
    URI tasksPrioritizedUrl = URI.create("http://localhost:8080/tasks/prioritized");

    @BeforeEach
    void beforeEach() throws IOException {
        httpTaskServer = new HttpTaskServer();
        httpTaskServer.createHttpTaskServer();
        httpClient = HttpClient.newHttpClient();
        gson = new Gson();
    }


    @Test
    void tasksTaskHandlerTestMethodsPostAndGet() throws IOException, InterruptedException { // тест tasksTaskHandler методов POST и GET
        Task task = new Task("Задача 1", "Описание задачи 1", StatusType.NEW, 30, "2023-03-04T09:00:00");
        task.setId(0);
        String toSend = gson.toJson(task);
        HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(toSend)).uri(tasksTaskUrl).build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        URI uriForTaskId0 = URI.create(tasksTaskUrl + "?id=0");
        request = HttpRequest.newBuilder().GET().uri(uriForTaskId0).build();
        response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Task taskFromServer = gson.fromJson(response.body(), Task.class);

        assertEquals(200, response.statusCode());
        assertEquals(task, taskFromServer);
    }

    @Test
    void tasksTaskHandlerTestMethodsDelete() throws IOException, InterruptedException { // тест tasksTaskHandler метод DELETE
        Task task = new Task("Задача 1", "Описание задачи 1", StatusType.NEW, 30, "2023-03-04T09:00:00");
        task.setId(0);
        String toSend = gson.toJson(task);
        HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(toSend)).uri(tasksTaskUrl).build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        URI uriForTaskId0 = URI.create(tasksTaskUrl + "?id=0");
        request = HttpRequest.newBuilder().DELETE().uri(uriForTaskId0).build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        request = HttpRequest.newBuilder().GET().uri(uriForTaskId0).build();
        response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals("null", response.body());
    }

    @Test
    void tasksSubtaskHandlerTestMethodsPostGetDelete() throws IOException, InterruptedException { // тест tasksSubtaskHandler методов POST, GET и DELETE
        Epic epic = new Epic("Эпик 1", "Описание эпика 1", StatusType.NEW);
        epic.setId(0);
        Subtask subtask = new Subtask("Подзадача 1", "Описание подазадачи 1", StatusType.NEW, 0, 20, "2023-03-03T09:00:00");
        subtask.setId(1);
        String epicToSend = gson.toJson(epic);
        String subtaskToSend = gson.toJson(subtask);
        HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(epicToSend)).uri(tasksEpicUrl).build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(subtaskToSend)).uri(tasksSubtaskUrl).build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        URI uriForSubtaskId1 = URI.create(tasksSubtaskUrl + "?id=1");
        request = HttpRequest.newBuilder().GET().uri(uriForSubtaskId1).build();
        response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Subtask subtaskFromServer = gson.fromJson(response.body(), Subtask.class);

        assertEquals(200, response.statusCode());
        assertEquals(subtask, subtaskFromServer);

        request = HttpRequest.newBuilder().DELETE().uri(uriForSubtaskId1).build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        request = HttpRequest.newBuilder().GET().uri(uriForSubtaskId1).build();
        response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals("null", response.body());
    }

    @Test
    void tasksSubtaskEpicHandlerTestMethodGet() throws IOException, InterruptedException { // тест tasksSubtaskEpicHandler метод GET
        Epic epic = new Epic("Эпик 1", "Описание эпика 1", StatusType.NEW);
        epic.setId(0);
        Subtask subtask = new Subtask("Подзадача 1", "Описание подазадачи 1", StatusType.NEW, 0, 20, "2023-03-03T09:00:00");
        subtask.setId(1);
        String epicToSend = gson.toJson(epic);
        String subtaskToSend = gson.toJson(subtask);
        HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(epicToSend)).uri(tasksEpicUrl).build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(subtaskToSend)).uri(tasksSubtaskUrl).build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        URI uriForSubtaskFromEpicId0 = URI.create(tasksSubtaskEpicUrl + "?id=0");
        request = HttpRequest.newBuilder().GET().uri(uriForSubtaskFromEpicId0).build();
        response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        Type listTypeToken = new TypeToken<Map<Integer, Subtask>>() {
        }.getType();
        Map<Integer, Subtask> subtasksFromEpic = gson.fromJson(response.body(), listTypeToken);
        Subtask subtaskFromServer = subtasksFromEpic.get(1);

        assertEquals(200, response.statusCode());
        assertEquals(subtask, subtaskFromServer);
    }

    @Test
    void tasksEpicHandlerTestMethodsPostGetDelete() throws IOException, InterruptedException { // тест tasksEpicHandler методов POST, GET и DELETE
        Epic epic = new Epic("Эпик 1", "Описание эпика 1", StatusType.NEW);
        epic.setId(0);
        String epicToSend = gson.toJson(epic);
        HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(epicToSend)).uri(tasksEpicUrl).build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        URI uriForEpicId0 = URI.create(tasksEpicUrl + "?id=0");
        request = HttpRequest.newBuilder().GET().uri(uriForEpicId0).build();
        response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        Epic epicFromServer = gson.fromJson(response.body(), Epic.class);

        assertEquals(200, response.statusCode());
        assertEquals(epic, epicFromServer);

        request = HttpRequest.newBuilder().DELETE().uri(uriForEpicId0).build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        request = HttpRequest.newBuilder().GET().uri(uriForEpicId0).build();
        response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals("null", response.body());
    }

    @Test
    void test001tasksHistoryHandlerTest() throws IOException, InterruptedException { // тест tasksHistoryHandler метод GET
        Task task0 = new Task("Задача 1", "Описание задачи 1", StatusType.NEW, 30, "2023-03-04T09:00:00");
        task0.setId(0);
        String toSend = gson.toJson(task0);
        HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(toSend)).uri(tasksTaskUrl).build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        Task task1 = new Task("Задача 2", "Описание задачи 2", StatusType.NEW, 30, "2023-03-05T09:00:00");
        task1.setId(1);
        toSend = gson.toJson(task1);
        request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(toSend)).uri(tasksTaskUrl).build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        Task task2 = new Task("Задача 3", "Описание задачи 3", StatusType.NEW, 30, "2023-03-06T09:00:00");
        task2.setId(2);
        toSend = gson.toJson(task2);
        request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(toSend)).uri(tasksTaskUrl).build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        URI uriForTaskId1 = URI.create(tasksTaskUrl + "?id=1");
        request = HttpRequest.newBuilder().GET().uri(uriForTaskId1).build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());


        URI uriForTaskId2 = URI.create(tasksTaskUrl + "?id=2");
        request = HttpRequest.newBuilder().GET().uri(uriForTaskId2).build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        URI uriForTaskId0 = URI.create(tasksTaskUrl + "?id=0");
        request = HttpRequest.newBuilder().GET().uri(uriForTaskId0).build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        request = HttpRequest.newBuilder().GET().uri(tasksHistoryUrl).build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        Type listTypeToken = new TypeToken<List<Task>>() {}.getType();
        List<Task> historyList = gson.fromJson(response.body(), listTypeToken);

        assertEquals(200, response.statusCode()); //
        assertEquals(task1, historyList.get(0));
        assertEquals(task2, historyList.get(1));
        assertEquals(task0, historyList.get(2));
    }


    @Test
    void tasksPrioritizedHandlerTest() throws IOException, InterruptedException {  // тест tasksPrioritizedHandler метод GET
        Task task0 = new Task("Задача 0", "Описание задачи 1", StatusType.NEW, 30, "2023-03-02T09:00:00");
        task0.setId(0);
        String toSend = gson.toJson(task0);
        HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(toSend)).uri(tasksTaskUrl).build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        Task task1 = new Task("Задача 1", "Описание задачи 2", StatusType.NEW, 30, "2023-03-03T09:00:00");
        task1.setId(1);
        toSend = gson.toJson(task1);
        request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(toSend)).uri(tasksTaskUrl).build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        Task task2 = new Task("Задача 2", "Описание задачи 3", StatusType.NEW, 30, "2023-03-01T09:00:00");
        task2.setId(2);
        toSend = gson.toJson(task2);
        request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofString(toSend)).uri(tasksTaskUrl).build();
        httpClient.send(request, HttpResponse.BodyHandlers.ofString());


        request = HttpRequest.newBuilder().GET().uri(tasksPrioritizedUrl).build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        Type setTypeToken = new TypeToken<Set<Task>>() {}.getType();
        Set<Task> prioritizedTasks = gson.fromJson(response.body(), setTypeToken);

        List<Task> tasksList = new ArrayList<>(prioritizedTasks);


        assertEquals(200, response.statusCode());
        assertEquals(task0, tasksList.get(1));
        assertEquals(task1, tasksList.get(2));
        assertEquals(task2, tasksList.get(0));
    }

    @AfterEach
    void afterEach() throws IOException {
        httpTaskServer.stopServer();
    }


}
