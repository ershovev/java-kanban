import java.util.HashMap;

public interface TaskManager {

    void newTask(Task task);   // создание нового таска

    void updateTask(int id, Task task);   // обновление таска

    Task getTask(int taskID);    // получение таска по идентификатору

    void printTask(Task task);  // печать таска

    HashMap<Integer, Task> getAllTasks();   // получение списка всех тасков

    void deleteTask(int taskID);   // удаление таска

    void deleteAllTasks();   // удаление всех тасков

    void newSubTask(Subtask subtask);   // создание нового сабтаска

    void updateSubtask(int id, Subtask subtask);    // обновление сабтаска

    Subtask getSubtask(int subtaskID);    // получение сабтаска по идентификатору

    void printSubtask(Subtask subtask);   // печать сабтаска
    HashMap<Integer, Subtask> getAllSubtasks();  // получение списка всех сабтасков

    void deleteSubtask(int subtaskID);   // удаление сабтаска и удаление его id из эпика

    void deleteAllSubtasks();      // удаление всех сабтасков и удаление их айди из эпиков

    void getAllSubtasksFromEpic(int epicID);  // получение всех сабтасков определенного эпика

    void newEpic(Epic epic);  // создание нового эпика

    void updateEpic(int id, Epic epic);  // обновление эпика

    Epic getEpic(int epicID); // получение эпика по идентификатору

    void printEpic(Epic epic); // печать эпика

    HashMap<Integer, Epic> getAllEpics();   // получение списка всех эпиков

    void deleteEpic(int epicID); // удаление эпика и всех его сабтасков

    void deleteAllEpics();   // удаление всех эпиков и их сабтасков

    void addSubTaskToEpic(int subtaskID, int epicID);    // добавление сабтаска к эпику

    StatusType newEpicStatus (int epicID); // проверка статуса сабтасков и возврат нового статуса эпика

    void calculateDurationAndTimeForEpic(int epicID); // расчет продолжительности и времени начала-окончания эпика
                                                   // на основании его сабтасков

    boolean isTaskOverlaps(Task task);

    void getPrioritizedTasks();   // вызов списка отсортированных по приоритету тасков
    boolean isEpicExists(int epicID);    // проверка существует ли эпик

    boolean isSubTaskExists(int subtaskID);   // проверка существует ли сабтаск

    boolean isTaskExists(int taskID);   // проверка существует ли таск

    StatusType checkSubtaskStatus (int subtaskID);   // проверка статуса сабтаска

    void printWatchedHistory();
}
