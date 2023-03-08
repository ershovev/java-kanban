package ru.yandex.practicum;

import ru.yandex.practicum.enums.StatusType;
import ru.yandex.practicum.serverclient.HttpTaskServer;
import ru.yandex.practicum.serverclient.KVServer;
import ru.yandex.practicum.taskmanager.FileBackedTasksManager;
import ru.yandex.practicum.taskmanager.HttpTaskManager;
import ru.yandex.practicum.tasktypes.Epic;
import ru.yandex.practicum.tasktypes.Subtask;
import ru.yandex.practicum.tasktypes.Task;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, FileBackedTasksManager.ManagerReadTaskException, FileBackedTasksManager.ManagerSaveException, InterruptedException {
        Managers managers = new Managers();
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.createHttpTaskServer();
        KVServer kvServer = new KVServer();
        kvServer.start();

        HttpTaskManager httpTaskManager = managers.getDefault("user1");

        System.out.println("Создаем различные таски\n");

        httpTaskManager.newTask(new Task("Задача 1", "Описание задачи 1", StatusType.NEW, 30, "2023-03-04T09:00:00"));
        httpTaskManager.newTask(new Task("Задача 2", "Описание задачи 2", StatusType.NEW, 70, "2023-03-02T10:00:00"));
        httpTaskManager.updateTask(1, new Task("Задача 2", "Описание задачи 2", StatusType.NEW, 70, "2023-03-02T10:20:00"));

        httpTaskManager.newEpic(new Epic("Эпик 1", "Описание эпика 1", StatusType.NEW));
        httpTaskManager.newSubTask(new Subtask("Подзадача 1", "Описание подазадачи 1", StatusType.NEW, 2, 20, "2023-03-03T09:00:00"));
        httpTaskManager.newSubTask(new Subtask("Подзадача 2", "Описание подазадачи 2", StatusType.NEW, 2, 60, "2023-03-03T10:00:00"));


        httpTaskManager.newTask(new Task("Задача 3", "Описание задачи 3", StatusType.NEW, 70, "2023-03-04T09:31:00"));

        httpTaskManager.getAllSubtasksFromEpic(2);

        System.out.println("Вызываем различные таски\n");

        httpTaskManager.getTask(0);
        httpTaskManager.getTask(1);
        httpTaskManager.getSubtask(4);
        httpTaskManager.getSubtask(3);

        System.out.println("\nСмотрим историю\n");

        httpTaskManager.printWatchedHistory();

        System.out.println("\nСоздаем новый httpTaskManager и загружаемся с сервера\n");

        httpTaskManager = managers.getDefault("user1");
        httpTaskManager.load();

        System.out.println("\nСмотрим восстановленную историю историю\n");

        httpTaskManager.printWatchedHistory();

        System.out.println("\nВызываем все восстановленные таски, эпики и сабтаски\n");

        httpTaskManager.getAllTasks();
        httpTaskManager.getAllEpics();
        httpTaskManager.getAllSubtasks();

        System.out.println("\nВызываем список отсортированных тасков\n");

        httpTaskManager.printPrioritizedTasks();
    }
}
