package ru.yandex.practicum.taskmanager;

import ru.yandex.practicum.tasktypes.Epic;
import ru.yandex.practicum.tasktypes.Subtask;
import ru.yandex.practicum.tasktypes.Task;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface TaskManager {

    void newTask(Task task);   // создание нового таска

    void updateTask(int id, Task task);   // обновление таска

    Task getTask(int taskId);    // получение таска по идентификатору

    void printTask(Task task);  // печать таска

    Map<Integer, Task> getAllTasks();   // получение списка всех тасков

    void deleteTask(int taskId);   // удаление таска

    void deleteAllTasks();   // удаление всех тасков

    void newSubTask(Subtask subtask);   // создание нового сабтаска

    void updateSubtask(int id, Subtask subtask);    // обновление сабтаска

    Subtask getSubtask(int subtaskId);    // получение сабтаска по идентификатору

    void printSubtask(Subtask subtask);   // печать сабтаска

    Map<Integer, Subtask> getAllSubtasks();  // получение списка всех сабтасков

    void deleteSubtask(int subtaskId);   // удаление сабтаска и удаление его id из эпика

    void deleteAllSubtasks();      // удаление всех сабтасков и удаление их айди из эпиков

    Map<Integer, Subtask> getAllSubtasksFromEpic(int epicId);  // получение всех сабтасков определенного эпика

    void newEpic(Epic epic);  // создание нового эпика

    void updateEpic(int id, Epic epic);  // обновление эпика

    Epic getEpic(int epicId); // получение эпика по идентификатору

    void printEpic(Epic epic); // печать эпика

    Map<Integer, Epic> getAllEpics();   // получение списка всех эпиков

    void deleteEpic(int epicId); // удаление эпика и всех его сабтасков

    void deleteAllEpics();   // удаление всех эпиков и их сабтасков

    Set<Task> printPrioritizedTasks();   // вызов списка отсортированных по приоритету тасков

    List<Task> printWatchedHistory();
}
