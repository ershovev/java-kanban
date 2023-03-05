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



/*        FileBackedTasksManager fileBackedTasksManager = managers.getFileBackedTasksManager();

        System.out.println("Создаем различные таски\n");

        fileBackedTasksManager.newTask(new Task("Задача 1", "Описание задачи 1", StatusType.NEW, 30, "2023-03-04T09:00:00"));
        fileBackedTasksManager.newTask(new Task("Задача 2", "Описание задачи 2", StatusType.NEW, 70, "2023-03-02T10:00:00"));
        fileBackedTasksManager.updateTask(1, new Task("Задача 2", "Описание задачи 2", StatusType.NEW, 70, "2023-03-02T10:20:00"));


        fileBackedTasksManager.newEpic(new Epic("Эпик 1", "Описание эпика 1", StatusType.NEW));
        fileBackedTasksManager.newSubTask(new Subtask("Подзадача 1", "Описание подазадачи 1", StatusType.NEW, 2, 20, "2023-03-03T09:00:00"));
        fileBackedTasksManager.newSubTask(new Subtask("Подзадача 2", "Описание подазадачи 2", StatusType.NEW, 2, 60, "2023-03-03T10:00:00"));


        fileBackedTasksManager.newTask(new Task("Задача 3", "Описание задачи 3", StatusType.NEW, 70, "2023-03-04T09:31:00"));

        fileBackedTasksManager.getAllSubtasksFromEpic(2);

        System.out.println("Вызываем различные таски\n");

        fileBackedTasksManager.getTask(0);
        fileBackedTasksManager.getTask(1);
        fileBackedTasksManager.getSubtask(4);
        fileBackedTasksManager.getSubtask(3);

        System.out.println("\nСмотрим историю\n");

        fileBackedTasksManager.printWatchedHistory();

        System.out.println("\nСоздаем новый fileBackedTasksManager и загружаемся с файла\n");

        FileBackedTasksManager fileBackedTasksManager2 = managers.getFileBackedTasksManager();
        try {
            fileBackedTasksManager2.load();
        } catch (FileBackedTasksManager.ManagerSaveException | FileBackedTasksManager.ManagerReadTaskException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("\nСмотрим восстановленную историю историю\n");

        fileBackedTasksManager2.printWatchedHistory();

        System.out.println("\nВызываем все восстановленные таски, эпики и сабтаски\n");

        fileBackedTasksManager2.getAllTasks();
        fileBackedTasksManager2.getAllEpics();
        fileBackedTasksManager2.getAllSubtasks();

        System.out.println("\nВызываем список отсортированных тасков\n");

        fileBackedTasksManager2.printPrioritizedTasks();

*/




/*        ru.yandex.practicum.taskmanager.TaskManager inMemoryTaskManager = managers.getInMemoryTaskManager();

        inMemoryTaskManager.newTask(new ru.yandex.practicum.tasktypes.Task("Задача 1", "Описание задачи 1", ru.yandex.practicum.enums.StatusType.NEW));
        inMemoryTaskManager.newTask(new ru.yandex.practicum.tasktypes.Task("Задача 2", "Описание задачи 2", ru.yandex.practicum.enums.StatusType.NEW));

        inMemoryTaskManager.newEpic(new ru.yandex.practicum.tasktypes.Epic("Эпик 1", "Описание эпика 1", ru.yandex.practicum.enums.StatusType.NEW));
        inMemoryTaskManager.newSubTask(new ru.yandex.practicum.tasktypes.Subtask("Подзадача 1", "Описание подазадачи 1", ru.yandex.practicum.enums.StatusType.NEW, 2));
        inMemoryTaskManager.newSubTask(new ru.yandex.practicum.tasktypes.Subtask("Подзадача 2", "Описание подазадачи 2", ru.yandex.practicum.enums.StatusType.NEW, 2));

        inMemoryTaskManager.newEpic(new ru.yandex.practicum.tasktypes.Epic("Эпик 2", "Описание эпика 2", ru.yandex.practicum.enums.StatusType.NEW));
        //  inMemoryTaskManager.newSubTask(new ru.yandex.practicum.tasktypes.Subtask("Подзадача 3", "Описание подазадачи 3", ru.yandex.practicum.enums.StatusType.NEW, 5));

        System.out.println("Просматриваем различные таски");
        inMemoryTaskManager.getTask(0);

        System.out.println("Выводим историю просмотра");
        inMemoryTaskManager.printWatchedHistory();

        inMemoryTaskManager.getSubtask(4);

        System.out.println("Выводим историю просмотра");
        inMemoryTaskManager.printWatchedHistory();

        inMemoryTaskManager.getSubtask(3);

        System.out.println("Выводим историю просмотра");
        inMemoryTaskManager.printWatchedHistory();

        inMemoryTaskManager.getEpic(2);

        System.out.println("Выводим историю просмотра");
        inMemoryTaskManager.printWatchedHistory();

        //    inMemoryTaskManager.getSubtask(6);

        inMemoryTaskManager.getEpic(5);

        System.out.println("Выводим историю просмотра");
        inMemoryTaskManager.printWatchedHistory();

        inMemoryTaskManager.getEpic(2);

        System.out.println("Выводим историю просмотра");
        inMemoryTaskManager.printWatchedHistory();

        //   inMemoryTaskManager.getSubtask(6);
        inMemoryTaskManager.getEpic(5);

        System.out.println("Выводим историю просмотра");
        inMemoryTaskManager.printWatchedHistory();

        inMemoryTaskManager.getEpic(2);

        System.out.println("Выводим историю просмотра");
        inMemoryTaskManager.printWatchedHistory();

        //   inMemoryTaskManager.getSubtask(6);

        inMemoryTaskManager.getEpic(5);

        System.out.println("Выводим историю просмотра");
        inMemoryTaskManager.printWatchedHistory();

        inMemoryTaskManager.getEpic(2);

        System.out.println("Выводим историю просмотра");
        inMemoryTaskManager.printWatchedHistory();

        //   inMemoryTaskManager.getSubtask(6);

        inMemoryTaskManager.getEpic(5);

        System.out.println("Выводим историю просмотра");
        inMemoryTaskManager.printWatchedHistory();

        System.out.println("Удаляем таск с ID 0 и снова выводим историю просмотров");
        inMemoryTaskManager.deleteTask(0);
        inMemoryTaskManager.printWatchedHistory();

        System.out.println("Удаляем эпик с ID 2 и снова выводим историю просмотров(помимо него должны быть удалены сабтаски с ID 3 и 4)");
        inMemoryTaskManager.deleteEpic(2);
        inMemoryTaskManager.printWatchedHistory();


//        inMemoryTaskManager.getAllTasks();
        //       inMemoryTaskManager.getAllSubtasks();
        //      inMemoryTaskManager.getAllEpics();


    /*    System.out.println("Обновляем подзадачу 2 и выводим ее и эпик");

        inMemoryTaskManager.updateSubtask(4, new ru.yandex.practicum.tasktypes.Subtask("Обновленная подзадача 2", "Описание обонвленной подзадачи 2",
                ru.yandex.practicum.enums.StatusType.DONE, 2));
        inMemoryTaskManager.getSubtask(4);
        inMemoryTaskManager.getEpic(2);

        System.out.println("Удаляем подзадачу 2 и выводим ее эпик");
        inMemoryTaskManager.deleteSubtask(4);
        inMemoryTaskManager.getEpic(2);

        System.out.println("Обновляем подзадачу 3 и выводим ее и эпик");
        inMemoryTaskManager.updateSubtask(6, new ru.yandex.practicum.tasktypes.Subtask("Обновленная подзадача 3", "Описание обновленной подзадачи 3",
                ru.yandex.practicum.enums.StatusType.DONE, 5));
        inMemoryTaskManager.getSubtask(6);
        inMemoryTaskManager.getEpic(5);

        System.out.println("Выводим все подзадачи");
        inMemoryTaskManager.getAllSubtasks();
        System.out.println("Удаляем эпик 1");
        inMemoryTaskManager.deleteEpic(2);
        System.out.println("Выводим все подзадачи снова");
        inMemoryTaskManager.getAllSubtasks(); */
    }
}
