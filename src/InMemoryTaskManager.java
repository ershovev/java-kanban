import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    protected int idCounter = 0;

    protected Managers managers = new Managers();
    protected HistoryManager inMemoryHistoryManager = managers.getDefaultHistory();

    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();



    @Override
    public void newTask(Task task) {   // создание нового таска
        task.id = idCounter;
        idCounter++;
        tasks.put(task.id, task);
    }

    @Override
    public void updateTask(int id, Task task) {   // обновление таска
        if (!isTaskExists(id)) {
            return;
        }
        task.id = id;
        tasks.put(id, task);
    }

    @Override
    public void getTask(int taskID) {   // получение таска по идентификатору
        if (!isTaskExists(taskID)) {
            return;
        }
        Task task = tasks.get(taskID);
        printTask(task);
        inMemoryHistoryManager.add(task); // добавляем таск в историю просмотров
    }

    @Override
    public void printTask(Task task) { // печать таска
        System.out.println("ID задачи: " + task.id + ", Название: " + task.name + ", Описание: " +
                task.description + ", Cтатус: " + task.status);
    }

    @Override
    public void getAllTasks() {  // получение списка всех тасков
        for (Task task : tasks.values()) {
            System.out.println("ID задачи: " + task.id + ", Название: " + task.name + ", Описание: " +
                    task.description + ", Cтатус: " + task.status);
        }
    }

    @Override
    public void deleteTask(int taskID) {  // удаление таска
        if (!isTaskExists(taskID)) {
            return;
        }
        tasks.remove(taskID);
        inMemoryHistoryManager.removeTaskFromHistory(taskID); // вызов метода удаление таска из истории просмотра
    }

    @Override
    public void deleteAllTasks() {  // удаление всех тасков
        for (Task task : tasks.values()) {
            inMemoryHistoryManager.removeTaskFromHistory(task.id); // вызов метода удаление таска из истории просмотра
        }
        tasks.clear();
    }

    @Override
    public void newSubTask(Subtask subtask) {   // создание нового сабтаска
        if (!isEpicExists(subtask.epicID)) {
            return;
        }
        subtask.id = idCounter;
        idCounter++;
        subtasks.put(subtask.id, subtask);
        addSubTaskToEpic(subtask.id, subtask.epicID);
    }

    @Override
    public void updateSubtask(int id, Subtask subtask) {   // обновление сабтаска
        if (!isSubTaskExists(id) || !isEpicExists(subtask.epicID)) {
            return;
        }
        subtask.id = id;
        subtasks.put(id, subtask);
        Epic epic = epics.get(subtask.epicID);
        epic.status = newEpicStatus(subtask.epicID);
        epics.put(epic.id, epic);
    }

    @Override
    public void getSubtask(int subtaskID) {    // получение сабтаска по идентификатору
        if (!isSubTaskExists(subtaskID)) {
            return;
        }
        Subtask subtask = subtasks.get(subtaskID);
        printSubtask(subtask);   // печатаем таск
        inMemoryHistoryManager.add(subtask);  // добавляем таск в историю просмотров
    }

    @Override
    public void printSubtask(Subtask subtask) { // печать  сабтаска
        System.out.println("ID подзадачи: " + subtask.id + ", Название: " + subtask.name + ", Описание: " +
                subtask.description + ", Cтатус: " + subtask.status + ", ID Эпика подзадачи: " + subtask.epicID);
    }

    @Override
    public void getAllSubtasks() {  // получение списка всех сабтасков
        for (Subtask subtask : subtasks.values()) {
            System.out.println("ID задачи: " + subtask.id + ", Название: " + subtask.name + ", Описание: " +
                    subtask.description + ", Cтатус: " + subtask.status + ", ID Эпика подзадачи: " + subtask.epicID);
        }
    }

    @Override
    public void deleteSubtask(int subtaskID) {    // удаление сабтаска и удаление его id из эпика
        if (!isSubTaskExists(subtaskID)) {
            return;
        }
        Subtask subtask = subtasks.get(subtaskID);
        Epic epic = epics.get(subtask.epicID);
        subtasks.remove(subtaskID);
        epic.tasks.remove(epic.tasks.indexOf(subtaskID));
        epics.put(subtask.epicID, epic);
        epic = epics.get(subtask.epicID);
        epic.status = newEpicStatus(subtask.epicID);
        epics.put(subtask.epicID, epic);
        inMemoryHistoryManager.removeTaskFromHistory(subtaskID); // вызов метода удаление таска из истории просмотра
    }

    @Override
    public void deleteAllSubtasks() {     // удаление всех сабтасков и удаление их айди из эпиков
        for (Subtask subtask : subtasks.values()) {
            Epic epic = epics.get(subtask.epicID);
            epic.tasks.remove(subtask.id);
            epics.put(epic.id, epic);
            inMemoryHistoryManager.removeTaskFromHistory(subtask.id); // вызов метода удаление таска из истории просмотра
        }
        subtasks.clear();
    }

    @Override
    public void getAllSubtasksFromEpic(int epicID) { // получение всех сабтасков определенного эпика
        Epic epic = epics.get(epicID);
        for (int subtaskID : epic.tasks) {
            Subtask subtask = subtasks.get(subtaskID);
            System.out.println("ID задачи: " + subtask.id + ", Название: " + subtask.name + ", Описание: " +
                    subtask.description + ", Cтатус: " + subtask.status);
        }
    }

    @Override
    public void newEpic(Epic epic) { // создание нового эпика
        epic.id = idCounter;
        idCounter++;
        epics.put(epic.id, epic);
    }

    @Override
    public void updateEpic(int id, Epic epic) { // обновление эпика
        if (!isEpicExists(id)) {
            return;
        }
        epic.id = id;
        epics.put(id, epic);
    }

    @Override
    public void getEpic(int epicID) { // получение эпика по идентификатору
        if (!isEpicExists(epicID)) {
            return;
        }
        Epic epic = epics.get(epicID);
        printEpic(epic);            // печатаем эпик
        inMemoryHistoryManager.add(epic); // добавляем таск в историю просмотров
    }

    @Override
    public void printEpic(Epic epic) {      // печать эпика
        System.out.println("ID эпика: " + epic.id + ", Название: " + epic.name + ", Описание: " +
                epic.description + ", Cтатус: " + epic.status);
    }

    @Override
    public void getAllEpics() {  // получение списка всех эпиков
        for (Epic epic : epics.values()) {
            System.out.println("ID эпика: " + epic.id + ", Название: " + epic.name + ", Описание: " +
                    epic.description + ", Cтатус: " + epic.status);
        }
    }

    @Override
    public void deleteEpic(int epicID) { // удаление эпика и всех его сабтасков
        if (!isEpicExists(epicID)) {
            return;
        }
        Epic epic = epics.get(epicID);
        for (int subtasksID : epic.tasks) {
            subtasks.remove(subtasksID);
            inMemoryHistoryManager.removeTaskFromHistory(subtasksID); // вызов метода удаление таска из истории просмотра
        }
        epics.remove(epicID);
        inMemoryHistoryManager.removeTaskFromHistory(epicID); // вызов метода удаление таска из истории просмотра
    }

    @Override
    public void deleteAllEpics() {  // удаление всех эпиков и их сабтасков
        for (Epic epic : epics.values()) {
            for (int subtasksID : epic.tasks) {
                subtasks.remove(subtasksID);
                inMemoryHistoryManager.removeTaskFromHistory(subtasksID); // вызов метода удаление таска из истории просмотра
            }
        }
        epics.clear();
    }

    @Override
    public void addSubTaskToEpic(int subtaskID, int epicID) {   // добавление сабтаска к эпику
        Epic epicToUpdate = epics.get(epicID);
        epicToUpdate.tasks.add(subtaskID);
        epicToUpdate.status = newEpicStatus(epicID);
        epics.put(epicID, epicToUpdate);
    }

    @Override
    public StatusType newEpicStatus (int epicID) { // проверка статуса сабтасков и возврат нового статуса эпика
        Epic epicToCheck = epics.get(epicID);
        int subtasksDone = 0;
        int subtasksNew = 0;
        StatusType statusToSet;

        for (Integer tasks : epicToCheck.tasks) {
            if (checkSubtaskStatus(tasks) == StatusType.DONE) {
                subtasksDone++;
            } else if (checkSubtaskStatus(tasks) == StatusType.NEW) {
                subtasksNew++;
            } else {
                return StatusType.IN_PROGRESS;
            }
        }

        if (subtasksDone > 0 && subtasksNew > 0) {
            statusToSet = StatusType.IN_PROGRESS;
        } else if (subtasksDone == 0) {
            statusToSet = StatusType.NEW;
        } else {
            statusToSet = StatusType.DONE;
        }

        return statusToSet;
    }

    @Override
    public boolean isEpicExists(int epicID) {   // проверка существует ли эпик
        boolean isEpicExists = false;
        if (epics.containsKey(epicID)) {
            isEpicExists = true;
        } else {
            System.out.println("Эпик с введенным ID отсутствует");
        }
        return isEpicExists;
    }

    @Override
    public boolean isSubTaskExists(int subtaskID) {  // проверка существует ли сабтаск
        boolean isSubTaskExists = false;
        if (subtasks.containsKey(subtaskID)) {
            isSubTaskExists = true;
        } else {
            System.out.println("Подзадача с введенным ID отсутствует");
        }
        return isSubTaskExists;
    }

    @Override
    public boolean isTaskExists(int taskID) {  // проверка существует ли таск
        boolean isTaskExists = false;
        if (tasks.containsKey(taskID)) {
            isTaskExists = true;
        } else {
            System.out.println("Задача с введенным ID отсутствует");
        }
        return isTaskExists;
    }

    @Override
    public StatusType checkSubtaskStatus (int subtaskID) {  // проверка статуса сабтаска
        Subtask subtask = subtasks.get(subtaskID);
        return subtask.status;
    }

    @Override
    public void printWatchedHistory() {    // печать списка просмотренных задач
        System.out.println("Последние просмотренные задачи:");
        ArrayList<Task> list = inMemoryHistoryManager.getHistory();
        for (Task task : list) {
            if (task instanceof Subtask) {
                Subtask subtask = (Subtask) task;
                printSubtask(subtask);
            } else if (task instanceof Epic) {
                Epic epic = (Epic) task;
                printEpic(epic);
            } else {
                printTask(task);
            }
        }
    }
}
