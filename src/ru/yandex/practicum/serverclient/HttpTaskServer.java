package ru.yandex.practicum.serverclient;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import ru.yandex.practicum.Managers;
import ru.yandex.practicum.taskmanager.FileBackedTasksManager;
import ru.yandex.practicum.tasktypes.Epic;
import ru.yandex.practicum.tasktypes.Subtask;
import ru.yandex.practicum.tasktypes.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class HttpTaskServer {

    private static final Managers managers = new Managers();
    private static final FileBackedTasksManager fileBackedTaskManager = managers.getFileBackedTasksManager();
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final Gson gson = new Gson();
    private static HttpServer httpServer;
    protected static final int PORT = 8080;

    public void createHttpTaskServer() throws IOException {
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks/task", new TasksTaskHandler());
        httpServer.createContext("/tasks/subtask", new TasksSubtaskHandler());
        httpServer.createContext("/tasks/subtask/epic", new TasksSubtaskEpicHandler());
        httpServer.createContext("/tasks/epic", new TasksEpicHandler());
        httpServer.createContext("/tasks/history", new TasksHistoryHandler());
        httpServer.createContext("/tasks/prioritized", new TasksPrioritizedHandler());
        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public void stopServer() {
        httpServer.stop(0);
        System.out.println("Сервер остановлен");
    }

    static class TasksTaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            System.out.println("Началась обработка /tasks/task запроса от клиента");

            String method = httpExchange.getRequestMethod();
            System.out.println("Началась обработка " + method + " метода, /tasks/task запроса от клиента.");

            String response = "";
            String path = String.valueOf(httpExchange.getRequestURI());

            switch (method) {
                case "GET":
                    if (path.contains("?")) {
                        String params = path.split("\\?")[1];
                        int taskId = Integer.parseInt(params.split("=")[1]);
                        response = gson.toJson(fileBackedTaskManager.getTask(taskId));
                    } else {
                        response = gson.toJson(fileBackedTaskManager.getAllTasks());
                    }

                    writeResponse(httpExchange, response, 200);
                    break;

                case "POST":
                    InputStream inputStream = httpExchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes());
                    Task task = gson.fromJson(body, Task.class);
                    fileBackedTaskManager.newTask(task);
                    writeResponse(httpExchange, response, 201);
                    break;

                case "DELETE":
                    if (path.contains("?")) {
                        String params = path.split("\\?")[1];
                        int taskId = Integer.parseInt(params.split("=")[1]);
                        fileBackedTaskManager.deleteTask(taskId);
                    } else {
                        fileBackedTaskManager.deleteAllTasks();
                    }

                    writeResponse(httpExchange, response, 200);
                    break;

                default:
                    writeResponse(httpExchange, "Такого эндпоинта не существует", 404);
            }
        }
    }

    static class TasksSubtaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            System.out.println("Началась обработка /tasks/subtask запроса от клиента");

            String method = httpExchange.getRequestMethod();
            System.out.println("Началась обработка " + method + " метода, /tasks/subtask запроса от клиента.");

            String response = "";
            String path = String.valueOf(httpExchange.getRequestURI());

            switch (method) {
                case "GET":
                    if (path.contains("?")) {
                        String params = path.split("\\?")[1];
                        int taskId = Integer.parseInt(params.split("=")[1]);
                        response = gson.toJson(fileBackedTaskManager.getSubtask(taskId));
                    } else {
                        response = gson.toJson(fileBackedTaskManager.getAllSubtasks());
                    }

                    writeResponse(httpExchange, response, 200);
                    break;

                case "POST":
                    InputStream inputStream = httpExchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes());
                    Subtask subtask = gson.fromJson(body, Subtask.class);
                    fileBackedTaskManager.newSubTask(subtask);
                    writeResponse(httpExchange, response, 201);
                    break;

                case "DELETE":
                    if (path.contains("?")) {
                        String params = path.split("\\?")[1];
                        int subtaskId = Integer.parseInt(params.split("=")[1]);
                        fileBackedTaskManager.deleteSubtask(subtaskId);
                    } else {
                        fileBackedTaskManager.deleteAllSubtasks();
                    }

                    writeResponse(httpExchange, response, 200);
                    break;

                default:
                    writeResponse(httpExchange, "Такого эндпоинта не существует", 404);
            }
        }
    }

    static class TasksSubtaskEpicHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            System.out.println("Началась обработка /tasks/subtask/epic запроса от клиента");

            String method = httpExchange.getRequestMethod();
            System.out.println("Началась обработка " + method + " метода, /tasks/subtask/epic запроса от клиента.");

            String response;
            String path = String.valueOf(httpExchange.getRequestURI());

            switch (method) {
                case "GET":
                    if (path.contains("?")) {
                        String params = path.split("\\?")[1];
                        int epicId = Integer.parseInt(params.split("=")[1]);
                        response = gson.toJson(fileBackedTaskManager.getAllSubtasksFromEpic(epicId));
                        writeResponse(httpExchange, response, 200);
                    } else {
                        response = "Необходим айди эпика";
                        writeResponse(httpExchange, response, 400);
                    }
                    break;

                default:
                    writeResponse(httpExchange, "Такого эндпоинта не существует", 404);
            }
        }
    }

    static class TasksEpicHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            System.out.println("Началась обработка /tasks/epic запроса от клиента");

            String method = httpExchange.getRequestMethod();
            System.out.println("Началась обработка " + method + " метода, /tasks/epic запроса от клиента.");

            String response = "";
            String path = String.valueOf(httpExchange.getRequestURI());

            switch (method) {
                case "GET":
                    if (path.contains("?")) {
                        String params = path.split("\\?")[1];
                        int epicId = Integer.parseInt(params.split("=")[1]);
                        response = gson.toJson(fileBackedTaskManager.getEpic(epicId));
                    } else {
                        response = gson.toJson(fileBackedTaskManager.getAllEpics());
                    }

                    writeResponse(httpExchange, response, 200);
                    break;

                case "POST":
                    InputStream inputStream = httpExchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes());
                    Epic epic = gson.fromJson(body, Epic.class);
                    fileBackedTaskManager.newEpic(epic);
                    writeResponse(httpExchange, response, 201);
                    break;

                case "DELETE":
                    if (path.contains("?")) {
                        String params = path.split("\\?")[1];
                        int epicId = Integer.parseInt(params.split("=")[1]);
                        fileBackedTaskManager.deleteEpic(epicId);
                    } else {
                        fileBackedTaskManager.deleteAllEpics();
                    }

                    writeResponse(httpExchange, response, 200);
                    break;

                default:
                    writeResponse(httpExchange, "Такого эндпоинта не существует", 404);
            }
        }
    }

    static class TasksHistoryHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            System.out.println("Началась обработка /tasks/history запроса от клиента");

            String method = httpExchange.getRequestMethod();
            System.out.println("Началась обработка " + method + " метода, /tasks/history запроса от клиента.");

            switch (method) {
                case "GET":
                    String response = gson.toJson(fileBackedTaskManager.getWatchedHistory());
                    writeResponse(httpExchange, response, 200);
                    break;

                default:
                    writeResponse(httpExchange, "Такого эндпоинта не существует", 404);
            }
        }
    }

    static class TasksPrioritizedHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            System.out.println("Началась обработка /tasks/prioritized запроса от клиента");

            String method = httpExchange.getRequestMethod();
            System.out.println("Началась обработка " + method + " метода, /tasks/prioritized запроса от клиента.");

            switch (method) {
                case "GET":
                    String response = gson.toJson(fileBackedTaskManager.getPrioritizedTasks());
                    writeResponse(httpExchange, response, 200);
                    break;

                default:
                    writeResponse(httpExchange, "Такого эндпоинта не существует", 404);
            }
        }
    }

    private static void writeResponse(HttpExchange exchange,
                                      String responseString,
                                      int responseCode) throws IOException {
        if (responseString.isBlank()) {
            exchange.sendResponseHeaders(responseCode, 0);
        } else {
            byte[] bytes = responseString.getBytes(DEFAULT_CHARSET);
            exchange.sendResponseHeaders(responseCode, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
        exchange.close();
    }
}