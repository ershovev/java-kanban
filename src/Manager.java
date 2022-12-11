import java.util.HashMap;

public class Manager {
    protected int idCounter = 0;

    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();


    public void newTask(String name, String description) {   // создание нового таска
        Task task = new Task(idCounter, name, description, "NEW");
        idCounter++;
        tasks.put(task.id, task);
    }

    public void updateTask(Task task) {   // обновление таска
        if (!isTaskExists(task.id)) {
            return;
        }
        tasks.put(task.id, task);
    }

    public void getTask(int taskID) {   // получение таска по идентификатору
        if (!isTaskExists(taskID)) {
            return;
        }
        Task task = tasks.get(taskID);
        System.out.println("ID задачи: " + task.id + ", Название: " + task.name + ", Описание: " +
                task.description + ", Cтатус: " + task.status);
    }

    public void getAllTasks() {  // получение списка всех тасков
        for (Task task : tasks.values()) {
            System.out.println("ID задачи: " + task.id + ", Название: " + task.name + ", Описание: " +
                    task.description + ", Cтатус: " + task.status);
        }
    }

    public void deleteTask(int taskID) {  // удаление таска
        if (!isTaskExists(taskID)) {
            return;
        }
        tasks.remove(taskID);
    }

    public void deleteAllTasks() {  // удаление всех тасков
        tasks.clear();
    }

    public void newSubTask(String name, String description, int epicID) {   // создание нового сабтаска
        if (!isEpicExists(epicID)) {
            return;
        }
        Subtask subtask = new Subtask(idCounter, name, description, "NEW", epicID);
        idCounter++;
        subtasks.put(subtask.id, subtask);
        addSubTaskToEpic(subtask.id, subtask.epicID);
    }

    public void updateSubtask(Subtask subtask) {   // обновление сабтаска
        if (!isSubTaskExists(subtask.id) || !isEpicExists(subtask.epicID)) {
            return;
        }
        subtasks.put(subtask.id, subtask);
        Epic epic = epics.get(subtask.epicID);
        epic.status = newEpicStatus(subtask.epicID);
        epics.put(epic.id, epic);
    }

    public void getSubtask(int subtaskID) {    // получение сабтаска по идентификатору
        if (!isSubTaskExists(subtaskID)) {
            return;
        }
        Subtask subtask = subtasks.get(subtaskID);
        System.out.println("ID задачи: " + subtask.id + ", Название: " + subtask.name + ", Описание: " +
                subtask.description + ", Cтатус: " + subtask.status + ", Эпик подзадачи: " + subtask.epicID);
    }

    public void getAllSubtasks() {  // получение списка всех сабтасков
        for (Subtask subtask : subtasks.values()) {
            System.out.println("ID задачи: " + subtask.id + ", Название: " + subtask.name + ", Описание: " +
                    subtask.description + ", Cтатус: " + subtask.status + ", Эпик подзадачи: " + subtask.epicID);
        }
    }

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
    }

    public void deleteAllSubtasks() {     // удаление всех сабтасков и удаление их айди из эпиков
        for (Subtask subtask : subtasks.values()) {
            Epic epic = epics.get(subtask.epicID);
            epic.tasks.remove(subtask.id);
            epics.put(epic.id, epic);
        }
        subtasks.clear();
    }

    public void getAllSubtasksFromEpic(int epicID) { // получение всех сабтасков определенного эпика
        Epic epic = epics.get(epicID);
        for (int subtaskID : epic.tasks) {
            Subtask subtask = subtasks.get(subtaskID);
            System.out.println("ID задачи: " + subtask.id + ", Название: " + subtask.name + ", Описание: " +
                    subtask.description + ", Cтатус: " + subtask.status);
        }
    }

    public void newEpic(String name, String description) { // создание нового эпика
        Epic epic = new Epic(idCounter, name, description, "NEW");
        idCounter++;
        epics.put(epic.id, epic);
    }

    public void updateEpic(Epic epic) { // обновление эпика
        if (!isEpicExists(epic.id)) {
            return;
        }
        epics.put(epic.id, epic);
    }

    public void getEpic(int epicID) { // получение эпика по идентификатору
        if (!isEpicExists(epicID)) {
            return;
        }
        Epic epic = epics.get(epicID);
        System.out.println("ID эпика: " + epic.id + ", Название: " + epic.name + ", Описание: " +
                epic.description + ", Cтатус: " + epic.status);
    }

    public void getAllEpics() {  // получение списка всех эпиков
        for (Epic epic : epics.values()) {
            System.out.println("ID эпика: " + epic.id + ", Название: " + epic.name + ", Описание: " +
                    epic.description + ", Cтатус: " + epic.status);
        }
    }

    public void deleteEpic(int epicID) { // удаление эпика и всех его сабтасков
        if (!isEpicExists(epicID)) {
            return;
        }
        Epic epic = epics.get(epicID);
        for (int subtasksID : epic.tasks) {
            subtasks.remove(subtasksID);
        }
        epics.remove(epicID);
    }

    public void deleteAllEpics() {  // удаление всех эпиков и их сабтасков
        for (Epic epic : epics.values()) {
            for (int subtasksID : epic.tasks) {
                subtasks.remove(subtasksID);
            }
        }
        epics.clear();
    }

    public void addSubTaskToEpic(int subtaskID, int epicID) {   // добавление сабтаска к эпику
        Epic epicToUpdate = epics.get(epicID);
        epicToUpdate.tasks.add(subtaskID);
        epicToUpdate.status = newEpicStatus(epicID);
        epics.put(epicID, epicToUpdate);
    }

    public String newEpicStatus (int epicID) { // проверка статуса сабтасков и возврат нового статуса эпика
        Epic epicToCheck = epics.get(epicID);
        int subtasksDone = 0;
        int subtasksNew = 0;
        String statusToSet = "";

        for (Integer tasks : epicToCheck.tasks) {
            if (checkSubtaskStatus(tasks).equals("DONE")) {
                subtasksDone++;
            } else if (checkSubtaskStatus(tasks).equals("NEW")) {
                subtasksNew++;
            } else {
                return "IN_PROGRESS";
            }
        }

        if (subtasksDone > 0 && subtasksNew > 0) {
            statusToSet = "IN_PROGRESS";
        } else if (subtasksDone == 0) {
            statusToSet = "NEW";
        } else {
            statusToSet = "DONE";
        }

        return statusToSet;
    }

    public boolean isEpicExists(int epicID) {   // проверка существует ли эпик
        boolean isEpicExists = false;
        if (epics.containsKey(epicID)) {
            isEpicExists = true;
        } else {
            System.out.println("Эпик с введенным ID отсутствует");
        }
        return isEpicExists;
    }

    public boolean isSubTaskExists(int subtaskID) {  // проверка существует ли сабтаск
        boolean isSubTaskExists = false;
        if (subtasks.containsKey(subtaskID)) {
            isSubTaskExists = true;
        } else {
            System.out.println("Подзадача с введенным ID отсутствует");
        }
        return isSubTaskExists;
    }

    public boolean isTaskExists(int taskID) {  // проверка существует ли таск
        boolean isTaskExists = false;
        if (tasks.containsKey(taskID)) {
            isTaskExists = true;
        } else {
            System.out.println("Задача с введенным ID отсутствует");
        }
        return isTaskExists;
    }

    public String checkSubtaskStatus (int subtaskID) {  // проверка статуса сабтаска
        Subtask subtask = subtasks.get(subtaskID);
        return subtask.status;
    }
}
