 import org.junit.jupiter.api.Test;
 import ru.yandex.practicum.enums.StatusType;
 import ru.yandex.practicum.taskmanager.TaskManager;
 import ru.yandex.practicum.tasktypes.Epic;
 import ru.yandex.practicum.tasktypes.Subtask;
 import ru.yandex.practicum.tasktypes.Task;

 import java.io.ByteArrayOutputStream;
 import java.io.PrintStream;
 import java.util.Map;

 import static org.junit.jupiter.api.Assertions.*;

 abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;

    @Test
    void newTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", StatusType.NEW, 30, "2023-03-04T09:00:00");
        taskManager.newTask(task);
        Task savedTask = taskManager.getTask(task.getId());

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(savedTask, task, "Задачи не совпадают.");
    }

    @Test
    void updateTask() {
        Task oldTask = new Task("Test addNewTask", "Test addNewTask description", StatusType.NEW, 30, "2023-03-04T09:00:00");
        taskManager.newTask(oldTask);
        Task task = new Task("Test addNewTask2", "Test addNewTask description2", StatusType.NEW, 50, "2023-03-05T09:00:00");
        taskManager.updateTask(0, task);
        Task savedTask = taskManager.getTask(0);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(savedTask, task, "Задачи не совпадают.");
    }

    @Test
    void printTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", StatusType.NEW, 30, "2023-03-04T09:00:00");
        taskManager.newTask(task);
        Task savedTask = taskManager.getTask(0);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        taskManager.printTask(savedTask);

        assertEquals("ID задачи: 0, Название: Test addNewTask, Описание: Test addNewTask description," +
                    " Cтатус: NEW, Продолжительность: 0 часов 30 минут, Дата и время начала: 2023-03-04T09:00, Дата и время окончания: 2023-03-04T09:30"
                + System.lineSeparator(), output.toString(), "Вывод не совпадает");
    }

    @Test
    void getAllTasks() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", StatusType.NEW, 30, "2023-03-04T09:00:00");
        Task task2 = new Task("Test addNewTask2", "Test addNewTask2 description", StatusType.NEW, 30, "2023-03-05T09:00:00");
        Task task3 = new Task("Test addNewTask3", "Test addNewTask3 description", StatusType.NEW, 30, "2023-03-06T09:00:00");
        taskManager.newTask(task);
        taskManager.newTask(task2);
        taskManager.newTask(task3);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        taskManager.getAllTasks();

        assertEquals("ID задачи: 0, Название: Test addNewTask, Описание: Test addNewTask description," +
                " Cтатус: NEW, Продолжительность: 0 часов 30 минут, Дата и время начала: 2023-03-04T09:00, Дата и время окончания: 2023-03-04T09:30" + System.lineSeparator() +
                "ID задачи: 1, Название: Test addNewTask2, Описание: Test addNewTask2 description," +
                " Cтатус: NEW, Продолжительность: 0 часов 30 минут, Дата и время начала: 2023-03-05T09:00, Дата и время окончания: 2023-03-05T09:30" + System.lineSeparator() +
                "ID задачи: 2, Название: Test addNewTask3, Описание: Test addNewTask3 description," +
                " Cтатус: NEW, Продолжительность: 0 часов 30 минут, Дата и время начала: 2023-03-06T09:00, Дата и время окончания: 2023-03-06T09:30" + System.lineSeparator(), output.toString(), "Вывод не совпадает");
    }

    @Test
    void deleteTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", StatusType.NEW, 30, "2023-03-04T09:00:00");
        taskManager.newTask(task);
        taskManager.deleteTask(0);
        Map<Integer, Task> tasks = taskManager.getAllTasks();

        assertEquals(0, tasks.size(),"Задача не удалена");
    }

    @Test
    void deleteAllTasks() {
        Task task = new Task("Test addNewTask", "Test addNewTask description",
                StatusType.NEW, 30, "2023-03-04T09:00:00");
        Task task2 = new Task("Test addNewTask", "Test addNewTask description",
                StatusType.NEW, 30, "2023-03-05T09:00:00");
        taskManager.newTask(task);
        taskManager.newTask(task2);
        taskManager.deleteAllTasks();
        Map<Integer, Task> tasks = taskManager.getAllTasks();

        assertEquals(0, tasks.size(),"Задачи не удалены полностью");
    }

    @Test
    void newSubTask() {
        Epic epic = new Epic("Test ru.yandex.practicum.tasktypes.Epic", "Test ru.yandex.practicum.tasktypes.Epic Description", StatusType.NEW);
        Subtask subtask = new Subtask("Test addNewTask", "Test addNewTask description",
                StatusType.NEW, 0, 30,"2023-03-04T09:00:00");
        taskManager.newEpic(epic);
        taskManager.newSubTask(subtask);
        Task savedSubtask = taskManager.getSubtask(subtask.getId());

        assertNotNull(savedSubtask, "Задача не найдена.");
        assertEquals(subtask, savedSubtask, "Задачи не совпадают.");
    }

    @Test
    void updateSubtask() {
        Epic epic = new Epic("Test ru.yandex.practicum.tasktypes.Epic", "Test ru.yandex.practicum.tasktypes.Epic Description", StatusType.NEW);
        Subtask subtask = new Subtask("Test addNewTask", "Test addNewTask description",
                StatusType.NEW, 0, 30,"2023-03-04T09:00:00");
        taskManager.newEpic(epic);
        taskManager.newSubTask(subtask);
        Subtask updatedSubtask = new Subtask("Test addNewTask2", "Test addNewTask description2",
                StatusType.NEW, 0, 50, "2023-03-05T09:00:00");
        taskManager.updateSubtask(1, updatedSubtask);
        Task savedSubtask = taskManager.getSubtask(subtask.getId());

        assertNotNull(savedSubtask, "Задача не найдена.");
        assertEquals(updatedSubtask, savedSubtask, "Задачи не совпадают.");
    }

    @Test
    void printSubtask() {
        Epic epic = new Epic("Test ru.yandex.practicum.tasktypes.Epic", "Test ru.yandex.practicum.tasktypes.Epic Description", StatusType.NEW);
        taskManager.newEpic(epic);
        Subtask subtask = new Subtask("Test addNewTask", "Test addNewTask description",
                StatusType.NEW,0, 30, "2023-03-04T09:00:00");
        taskManager.newSubTask(subtask);
        Subtask savedSubtask = taskManager.getSubtask(1);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        taskManager.printSubtask(savedSubtask);

        assertEquals("ID подзадачи: 1, Название: Test addNewTask, Описание: Test addNewTask description," +
                " Cтатус: NEW, ID Эпика подзадачи: 0, Продолжительность: 0 часов 30 минут, Дата и время начала: 2023-03-04T09:00, Дата и время окончания: 2023-03-04T09:30"
                + System.lineSeparator(), output.toString(), "Вывод не совпадает");
    }

    @Test
    void getAllSubtasks() {
        Epic epic = new Epic("Test ru.yandex.practicum.tasktypes.Epic", "Test ru.yandex.practicum.tasktypes.Epic Description", StatusType.NEW);
        taskManager.newEpic(epic);
        Subtask subtask = new Subtask("Test addNewTask", "Test addNewTask description", StatusType.NEW, 0,30, "2023-03-04T09:00:00");
        Subtask subtask2 = new Subtask("Test addNewTask2", "Test addNewTask2 description", StatusType.NEW, 0,30, "2023-03-05T09:00:00");
        Subtask subtask3 = new Subtask("Test addNewTask3", "Test addNewTask3 description", StatusType.NEW, 0,30, "2023-03-06T09:00:00");
        taskManager.newSubTask(subtask);
        taskManager.newSubTask(subtask2);
        taskManager.newSubTask(subtask3);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        taskManager.getAllSubtasks();

        assertEquals("ID подзадачи: 1, Название: Test addNewTask, Описание: Test addNewTask description," +
                " Cтатус: NEW, ID Эпика подзадачи: 0, Продолжительность: 0 часов 30 минут, Дата и время начала: 2023-03-04T09:00, Дата и время окончания: 2023-03-04T09:30" + System.lineSeparator() +
                "ID подзадачи: 2, Название: Test addNewTask2, Описание: Test addNewTask2 description," +
                " Cтатус: NEW, ID Эпика подзадачи: 0, Продолжительность: 0 часов 30 минут, Дата и время начала: 2023-03-05T09:00, Дата и время окончания: 2023-03-05T09:30" + System.lineSeparator() +
                "ID подзадачи: 3, Название: Test addNewTask3, Описание: Test addNewTask3 description," +
                " Cтатус: NEW, ID Эпика подзадачи: 0, Продолжительность: 0 часов 30 минут, Дата и время начала: 2023-03-06T09:00, Дата и время окончания: 2023-03-06T09:30" + System.lineSeparator(), output.toString(), "Вывод не совпадает");
    }

    @Test
    void deleteSubtask() {
        Epic epic = new Epic("Test ru.yandex.practicum.tasktypes.Epic", "Test ru.yandex.practicum.tasktypes.Epic Description", StatusType.NEW);
        Subtask subtask = new Subtask("Test addNewTask", "Test addNewTask description",
                StatusType.NEW, 0, 30,"2023-03-04T09:00:00");
        taskManager.newEpic(epic);
        taskManager.newSubTask(subtask);
        taskManager.deleteSubtask(1);
        Map<Integer, Subtask> tasks = taskManager.getAllSubtasks();

        assertEquals(0, tasks.size(),"Задача не удалена");
    }

    @Test
    void deleteAllSubtasks() {
        Epic epic = new Epic("Test ru.yandex.practicum.tasktypes.Epic", "Test ru.yandex.practicum.tasktypes.Epic Description", StatusType.NEW);
        Subtask subtask = new Subtask("Test addNewTask", "Test addNewTask description",
                StatusType.NEW, 0, 30,"2023-03-04T09:00:00");
        Subtask subtask2 = new Subtask("Test addNewTask2", "Test addNewTask2 description",
                StatusType.NEW, 0, 30,"2023-03-05T09:00:00");
        taskManager.newEpic(epic);
        taskManager.newSubTask(subtask);
        taskManager.newSubTask(subtask2);
        taskManager.deleteAllSubtasks();
        Map<Integer, Subtask> tasks = taskManager.getAllSubtasks();

        assertEquals(0, tasks.size(),"Задача не удалена");
    }

    @Test
    void getAllSubtasksFromEpic() {
        Epic epic = new Epic("Test ru.yandex.practicum.tasktypes.Epic", "Test ru.yandex.practicum.tasktypes.Epic Description", StatusType.NEW);
        taskManager.newEpic(epic);
        Subtask subtask = new Subtask("Test addNewTask", "Test addNewTask description", StatusType.NEW, 0,30, "2023-03-04T09:00:00");
        Subtask subtask2 = new Subtask("Test addNewTask2", "Test addNewTask2 description", StatusType.NEW, 0,30, "2023-03-05T09:00:00");
        Subtask subtask3 = new Subtask("Test addNewTask3", "Test addNewTask3 description", StatusType.NEW, 0,30, "2023-03-06T09:00:00");
        taskManager.newSubTask(subtask);
        taskManager.newSubTask(subtask2);
        taskManager.newSubTask(subtask3);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        taskManager.getAllSubtasksFromEpic(0);

        assertEquals("ID подзадачи: 1, Название: Test addNewTask, Описание: Test addNewTask description," +
                " Cтатус: NEW, ID Эпика подзадачи: 0, Продолжительность: 0 часов 30 минут, Дата и время начала: 2023-03-04T09:00, Дата и время окончания: 2023-03-04T09:30" + System.lineSeparator() +
                "ID подзадачи: 2, Название: Test addNewTask2, Описание: Test addNewTask2 description," +
                " Cтатус: NEW, ID Эпика подзадачи: 0, Продолжительность: 0 часов 30 минут, Дата и время начала: 2023-03-05T09:00, Дата и время окончания: 2023-03-05T09:30" + System.lineSeparator() +
                "ID подзадачи: 3, Название: Test addNewTask3, Описание: Test addNewTask3 description," +
                " Cтатус: NEW, ID Эпика подзадачи: 0, Продолжительность: 0 часов 30 минут, Дата и время начала: 2023-03-06T09:00, Дата и время окончания: 2023-03-06T09:30" + System.lineSeparator(), output.toString(), "Вывод не совпадает");
    }

    @Test
    void newEpic() {
        Epic epic = new Epic("Test ru.yandex.practicum.tasktypes.Epic", "Test ru.yandex.practicum.tasktypes.Epic Description", StatusType.NEW);
        taskManager.newEpic(epic);
        Epic savedEpic = taskManager.getEpic(epic.getId());

        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");
    }

    @Test
    void updateEpic() {
        Epic epic = new Epic("Test ru.yandex.practicum.tasktypes.Epic", "Test ru.yandex.practicum.tasktypes.Epic Description", StatusType.NEW);
        taskManager.newEpic(epic);

        Epic updatedEpic = new Epic("Test EpicUpdated", "Test EpicUpdated Description", StatusType.NEW);
        taskManager.updateEpic(0, updatedEpic);
        Epic savedUpdatedEpic = taskManager.getEpic(epic.getId());

        assertNotNull(savedUpdatedEpic, "Задача не найдена.");
        assertEquals(updatedEpic, savedUpdatedEpic, "Задачи не совпадают.");
    }

    @Test
    void printEpic() {
        Epic epic = new Epic("Test ru.yandex.practicum.tasktypes.Epic", "Test ru.yandex.practicum.tasktypes.Epic Description", StatusType.NEW);
        taskManager.newEpic(epic);
        Subtask subtask = new Subtask("Test addNewTask", "Test addNewTask description", StatusType.NEW, 0,30, "2023-03-04T09:00:00");
        taskManager.newSubTask(subtask);
        Epic savedEpic = taskManager.getEpic(0);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        taskManager.printEpic(savedEpic);

        assertEquals("ID эпика: 0, Название: Test ru.yandex.practicum.tasktypes.Epic, Описание: Test ru.yandex.practicum.tasktypes.Epic Description," +
                " Cтатус: NEW,  Продолжительность: 0 часов 30 минут, Дата и время начала: 2023-03-04T09:00, Дата и время окончания: 2023-03-04T09:30"
                + System.lineSeparator(), output.toString(), "Вывод не совпадает");
    }

    @Test
    void getAllEpics() {
        Epic epic = new Epic("Test ru.yandex.practicum.tasktypes.Epic", "Test ru.yandex.practicum.tasktypes.Epic Description", StatusType.NEW);
        Epic epic2 = new Epic("Test Epic2", "Test Epic2 Description", StatusType.NEW);
        taskManager.newEpic(epic);
        taskManager.newEpic(epic2);
        Subtask subtask = new Subtask("Test addNewTask", "Test addNewTask description", StatusType.NEW, 0,30, "2023-03-04T09:00:00");
        Subtask subtask2 = new Subtask("Test addNewTask2", "Test addNewTask2 description", StatusType.NEW, 1,30, "2023-03-05T09:00:00");
        taskManager.newSubTask(subtask);
        taskManager.newSubTask(subtask2);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        taskManager.getAllEpics();

        assertEquals("ID эпика: 0, Название: Test ru.yandex.practicum.tasktypes.Epic, Описание: Test ru.yandex.practicum.tasktypes.Epic Description," +
                " Cтатус: NEW,  Продолжительность: 0 часов 30 минут, Дата и время начала: 2023-03-04T09:00, Дата и время окончания: 2023-03-04T09:30"
                + System.lineSeparator() + "ID эпика: 1, Название: Test Epic2, Описание: Test Epic2 Description," +
                " Cтатус: NEW,  Продолжительность: 0 часов 30 минут, Дата и время начала: 2023-03-05T09:00, Дата и время окончания: 2023-03-05T09:30" + System.lineSeparator(),
                output.toString(), "Вывод не совпадает");
    }

    @Test
    void deleteEpic() {
        Epic epic = new Epic("Test addNewTask", "Test addNewTask description", StatusType.NEW);
        taskManager.newEpic(epic);
        taskManager.deleteEpic(0);
        Map<Integer, Epic> epics = taskManager.getAllEpics();

        assertEquals(0, epics.size(),"Задача не удалена");
    }

    @Test
    void deleteAllEpics() {
        Epic epic = new Epic("Test addNewTask", "Test addNewTask description", StatusType.NEW);
        Epic epic2 = new Epic("Test addNewTask2", "Test addNewTask2 description", StatusType.NEW);
        taskManager.newEpic(epic);
        taskManager.newEpic(epic2);
        taskManager.deleteAllEpics();
        Map<Integer, Epic> epics = taskManager.getAllEpics();

        assertEquals(0, epics.size(),"Задачи не удалены");
    }

    @Test
    void newEpicStatus() {
        Epic epic = new Epic("Test ru.yandex.practicum.tasktypes.Epic", "Test ru.yandex.practicum.tasktypes.Epic Description", StatusType.NEW);
        taskManager.newEpic(epic);
        Epic savedEpic = taskManager.getEpic(0);
        assertEquals(StatusType.NEW, savedEpic.getStatus(), "Статусы не совпадают");

        Subtask subtask = new Subtask("Test addNewTask", "Test addNewTask description", StatusType.NEW, 0,30, "2023-03-04T09:00:00");
        Subtask subtask2 = new Subtask("Test addNewTask", "Test addNewTask description", StatusType.NEW, 0,30, "2023-03-05T09:00:00");
        taskManager.newSubTask(subtask);
        taskManager.newSubTask(subtask2);
        Epic savedEpic2 = taskManager.getEpic(0);
        assertEquals(StatusType.NEW, savedEpic2.getStatus(), "Статусы не совпадают");

        subtask = new Subtask("Test addNewTask", "Test addNewTask description", StatusType.DONE, 0,30, "2023-03-04T09:00:00");
        subtask2 = new Subtask("Test addNewTask", "Test addNewTask description", StatusType.DONE, 0,30, "2023-03-05T09:00:00");
        taskManager.updateSubtask(1, subtask);
        taskManager.updateSubtask(2, subtask2);
        Epic savedEpic3 = taskManager.getEpic(0);
        assertEquals(StatusType.DONE, savedEpic3.getStatus(), "Статусы не совпадают");

        subtask = new Subtask("Test addNewTask", "Test addNewTask description", StatusType.NEW, 0,30, "2023-03-04T09:00:00");
        taskManager.updateSubtask(1, subtask);
        Epic savedEpic4 = taskManager.getEpic(0);
        assertEquals(StatusType.IN_PROGRESS, savedEpic4.getStatus(), "Статусы не совпадают");

        subtask = new Subtask("Test addNewTask", "Test addNewTask description", StatusType.IN_PROGRESS, 0,30, "2023-03-04T09:00:00");
        taskManager.updateSubtask(1, subtask);
        Epic savedEpic5 = taskManager.getEpic(0);
        assertEquals(StatusType.IN_PROGRESS, savedEpic5.getStatus(), "Статусы не совпадают");
    }

    @Test
    void getPrioritizedTasks() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", StatusType.NEW, 30, "2023-03-08T09:00:00");
        Epic epic = new Epic("Test addNewTask2", "Test addNewTask2 description", StatusType.NEW);
        Subtask subtask = new Subtask("Test addNewTask3", "Test addNewTask3 description", StatusType.NEW, 1,30, "2023-03-06T09:00:00");
        taskManager.newTask(task);
        taskManager.newEpic(epic);
        taskManager.newSubTask(subtask);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        taskManager.printPrioritizedTasks();

        assertEquals("Список задач, отсортированных по важности:\n" + System.lineSeparator() + "ID подзадачи: 2, Название: Test addNewTask3, Описание: Test addNewTask3 description, Cтатус: NEW," +
                        " ID Эпика подзадачи: 1, Продолжительность: 0 часов 30 минут, Дата и время начала: 2023-03-06T09:00, Дата и время окончания: 2023-03-06T09:30"
                        + System.lineSeparator() + "ID задачи: 0, Название: Test addNewTask, Описание: Test addNewTask description, Cтатус: NEW, Продолжительность: 0 часов 30 минут," +
                        " Дата и время начала: 2023-03-08T09:00, Дата и время окончания: 2023-03-08T09:30" + System.lineSeparator(),
                output.toString(), "Вывод не совпадает");
    }

    @Test
    void printWatchedHistory() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", StatusType.NEW, 30, "2023-03-08T09:00:00");
        Epic epic = new Epic("Test addNewTask2", "Test addNewTask2 description", StatusType.NEW);
        Subtask subtask = new Subtask("Test addNewTask3", "Test addNewTask3 description", StatusType.NEW, 1,30, "2023-03-06T09:00:00");
        taskManager.newTask(task);
        taskManager.newEpic(epic);
        taskManager.newSubTask(subtask);

        taskManager.getTask(0);
        taskManager.getSubtask(2);
        taskManager.getEpic(1);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        taskManager.printWatchedHistory();

        assertEquals("Последние просмотренные задачи:" + System.lineSeparator() +  "ID задачи: 0, Название: Test addNewTask, Описание: Test addNewTask description," +
                " Cтатус: NEW, Продолжительность: 0 часов 30 минут, Дата и время начала: 2023-03-08T09:00, Дата и время окончания: 2023-03-08T09:30" + System.lineSeparator() +
                "ID подзадачи: 2, Название: Test addNewTask3, Описание: Test addNewTask3 description, Cтатус: NEW, ID Эпика подзадачи: 1, Продолжительность: 0 часов 30 минут," +
                " Дата и время начала: 2023-03-06T09:00, Дата и время окончания: 2023-03-06T09:30" + System.lineSeparator() + "ID эпика: 1, Название: Test addNewTask2, Описание:" +
                " Test addNewTask2 description, Cтатус: NEW,  Продолжительность: 0 часов 30 минут, Дата и время начала: 2023-03-06T09:00, Дата и время окончания: 2023-03-06T09:30"
                + System.lineSeparator(), output.toString(), "Вывод не совпадает");
    }
}