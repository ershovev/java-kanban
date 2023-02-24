import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.Managers;
import ru.yandex.practicum.enums.StatusType;
import ru.yandex.practicum.taskmanager.FileBackedTasksManager;
import ru.yandex.practicum.tasktypes.Epic;
import ru.yandex.practicum.tasktypes.Subtask;
import ru.yandex.practicum.tasktypes.Task;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    @BeforeEach
    public void beforeEach() {
        Managers managers = new Managers();
        taskManager = managers.getFileBackedTasksManager();
    }


    @Test
    void loadFromFile() {
        Managers managers = new Managers();
        taskManager = managers.getFileBackedTasksManager();
        taskManager.newTask(new Task("Задача 1", "Описание задачи 1", StatusType.NEW, 30, "2023-03-04T09:00:00"));
        taskManager.newTask(new Task("Задача 2", "Описание задачи 2", StatusType.NEW, 70, "2023-03-02T10:00:00"));
        taskManager.newEpic(new Epic("Эпик 1", "Описание эпика 1", StatusType.NEW));
        taskManager.newSubTask(new Subtask("Подзадача 1", "Описание подазадачи 1", StatusType.NEW, 2, 20, "2023-03-03T09:00:00"));
        taskManager.newSubTask(new Subtask("Подзадача 2", "Описание подазадачи 2", StatusType.NEW, 2, 60, "2023-03-03T10:00:00"));

        taskManager.getTask(0);
        taskManager.getSubtask(4);
        taskManager.getSubtask(3);

        taskManager.printWatchedHistory();

        FileBackedTasksManager fileBackedTasksManager2 = managers.getFileBackedTasksManager();
        try {
            fileBackedTasksManager2.loadFromFile();
        } catch (FileBackedTasksManager.ManagerSaveException | FileBackedTasksManager.ManagerReadTaskException e) {
            System.out.println(e.getMessage());
        }

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        fileBackedTasksManager2.printWatchedHistory();
        fileBackedTasksManager2.getAllTasks();
        fileBackedTasksManager2.getAllEpics();
        fileBackedTasksManager2.getAllSubtasks();
        fileBackedTasksManager2.getPrioritizedTasks();

        assertEquals("Последние просмотренные задачи:" + System.lineSeparator() + "ID задачи: 0, Название: Задача 1, Описание: Описание задачи 1, Cтатус: NEW," +
                " Продолжительность: 0 часов 30 минут, Дата и время начала: 2023-03-04T09:00, Дата и время окончания: 2023-03-04T09:30" + System.lineSeparator() +
                "ID подзадачи: 4, Название: Подзадача 2, Описание: Описание подазадачи 2, Cтатус: NEW, ID Эпика подзадачи: 2, Продолжительность: 1 часов 0 минут," +
                " Дата и время начала: 2023-03-03T10:00, Дата и время окончания: 2023-03-03T11:00" + System.lineSeparator() + "ID подзадачи: 3, Название: Подзадача 1," +
                " Описание: Описание подазадачи 1, Cтатус: NEW, ID Эпика подзадачи: 2, Продолжительность: 0 часов 20 минут, Дата и время начала: 2023-03-03T09:00," +
                " Дата и время окончания: 2023-03-03T09:20" + System.lineSeparator() + "ID задачи: 0, Название: Задача 1, Описание: Описание задачи 1, Cтатус: NEW," +
                " Продолжительность: 0 часов 30 минут, Дата и время начала: 2023-03-04T09:00, Дата и время окончания: 2023-03-04T09:30" + System.lineSeparator() +
                "ID задачи: 1, Название: Задача 2, Описание: Описание задачи 2, Cтатус: NEW, Продолжительность: 1 часов 10 минут, Дата и время начала: 2023-03-02T10:00," +
                " Дата и время окончания: 2023-03-02T11:10" + System.lineSeparator() +
                "ID эпика: 2, Название: Эпик 1, Описание: Описание эпика 1, Cтатус: NEW,  Продолжительность: 1 часов 20 минут, Дата и время начала: 2023-03-03T09:00," +
                " Дата и время окончания: 2023-03-03T11:00" + System.lineSeparator() + "ID подзадачи: 3, Название: Подзадача 1, Описание: Описание подазадачи 1, Cтатус: NEW," +
                " ID Эпика подзадачи: 2, Продолжительность: 0 часов 20 минут, Дата и время начала: 2023-03-03T09:00, Дата и время окончания: 2023-03-03T09:20" +
                System.lineSeparator() + "ID подзадачи: 4, Название: Подзадача 2, Описание: Описание подазадачи 2, Cтатус: NEW, ID Эпика подзадачи: 2," +
                " Продолжительность: 1 часов 0 минут, Дата и время начала: 2023-03-03T10:00, Дата и время окончания: 2023-03-03T11:00" + System.lineSeparator() +
                "Список задач, отсортированных по важности:\n" + System.lineSeparator() + "ID задачи: 1, Название: Задача 2, Описание: Описание задачи 2, Cтатус: NEW," +
                " Продолжительность: 1 часов 10 минут, Дата и время начала: 2023-03-02T10:00, Дата и время окончания: 2023-03-02T11:10" + System.lineSeparator() +
                "ID подзадачи: 3, Название: Подзадача 1, Описание: Описание подазадачи 1, Cтатус: NEW, ID Эпика подзадачи: 2, Продолжительность: 0 часов 20 минут," +
                " Дата и время начала: 2023-03-03T09:00, Дата и время окончания: 2023-03-03T09:20" + System.lineSeparator() + "ID подзадачи: 4, Название: Подзадача 2," +
                " Описание: Описание подазадачи 2, Cтатус: NEW, ID Эпика подзадачи: 2, Продолжительность: 1 часов 0 минут, Дата и время начала: 2023-03-03T10:00," +
                " Дата и время окончания: 2023-03-03T11:00" + System.lineSeparator() + "ID задачи: 0, Название: Задача 1, Описание: Описание задачи 1, Cтатус: NEW," +
                " Продолжительность: 0 часов 30 минут, Дата и время начала: 2023-03-04T09:00, Дата и время окончания: 2023-03-04T09:30" + System.lineSeparator(),
                output.toString(), "Вывод после загрузки из файла не совпадает");
    }
}