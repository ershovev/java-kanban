package ru.yandex.practicum.taskmanager;

import ru.yandex.practicum.Managers;
import ru.yandex.practicum.enums.StatusType;
import ru.yandex.practicum.historymanager.HistoryManager;
import ru.yandex.practicum.tasktypes.Epic;
import ru.yandex.practicum.tasktypes.Subtask;
import ru.yandex.practicum.tasktypes.Task;

import java.time.LocalDateTime;
import java.util.*;
import java.time.Duration;

public class InMemoryTaskManager implements TaskManager {


    private int idCounter = 0;

    protected Managers managers = new Managers();
    protected HistoryManager inMemoryHistoryManager = managers.getDefaultHistory();


    private Map<Integer, Task> tasks = new HashMap<>();


    private Map<Integer, Subtask> subtasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();


    private Set<Task> prioritizedTasks = new TreeSet<>(new TasksByTimeComparator()); // TreeSet для хранения отсортированных по приоритету тасков


    class TasksByTimeComparator implements Comparator<Task>  // компаратор для сортировки тасков по дате/времени начала
    {
        public int compare(Task task1, Task task2) {
            return task1.getStartTime().compareTo(task2.getStartTime());
        }
    }

    public int getIdCounter() {
        return idCounter;
    }

    public void setIdCounter(int idCounter) {
        this.idCounter = idCounter;
    }

    public Map<Integer, Task> getTasks() {
        return tasks;
    }

    public Map<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public Map<Integer, Epic> getEpics() {
        return epics;
    }

    public Set<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }


    @Override
    public void newTask(Task task) {   // создание нового таска
        if (!isTaskOverlaps(task)) {
            if (task.getId() == null) {
                task.setId(idCounter);
                idCounter++;
            }
            tasks.put(task.getId(), task);
            prioritizedTasks.add(task);
        }
    }

    @Override
    public void updateTask(int id, Task task) {   // обновление таска
        if (!isTaskExists(id)) {
            return;
        }
        task.setId(id);
        if (!isTaskOverlaps(task)) {
            tasks.put(id, task);
            prioritizedTasks.add(task);
        }
    }

    @Override
    public Task getTask(int taskId) {   // получение таска по идентификатору
        if (!isTaskExists(taskId)) {
            return null;
        }
        Task task = tasks.get(taskId);
        printTask(task);
        inMemoryHistoryManager.add(task); // добавляем таск в историю просмотров
        return task;
    }

    @Override
    public void printTask(Task task) { // печать таска
        System.out.println("ID задачи: " + task.getId() + ", Название: " + task.getName() + ", Описание: " +
                task.getDescription() + ", Cтатус: " + task.getStatus() + ", Продолжительность: " + task.getDuration().toHours() +
                " часов " + task.getDuration().toMinutesPart() + " минут, " +
                "Дата и время начала: " + task.getStartTime() + ", Дата и время окончания: " + task.getEndTime());
    }

    @Override
    public Map<Integer, Task> getAllTasks() {  // получение списка всех тасков
        for (Task task : tasks.values()) {
            printTask(task);
        }
        return tasks;
    }

    @Override
    public void deleteTask(int taskId) {  // удаление таска
        if (!isTaskExists(taskId)) {
            return;
        }
        prioritizedTasks.remove(tasks.get(taskId));
        tasks.remove(taskId);
        inMemoryHistoryManager.removeTaskFromHistory(taskId); // вызов метода удаление таска из истории просмотра
    }

    @Override
    public void deleteAllTasks() {  // удаление всех тасков
        for (Task task : tasks.values()) {
            inMemoryHistoryManager.removeTaskFromHistory(task.getId()); // вызов метода удаление таска из истории просмотра
            prioritizedTasks.remove(tasks.get(task.getId()));
        }
        tasks.clear();
        System.out.println("Все таски удалены");
    }

    @Override
    public void newSubTask(Subtask subtask) {   // создание нового сабтаска
        if (!isEpicExists(subtask.getEpicId())) {
            return;
        }
        if (!isTaskOverlaps(subtask)) {
            if (subtask.getId() == null) {
                subtask.setId(idCounter);
                idCounter++;
            }
            subtasks.put(subtask.getId(), subtask);
            addSubTaskToEpic(subtask.getId(), subtask.getEpicId());
            calculateDurationAndTimeForEpic(subtask.getEpicId());
            prioritizedTasks.add(subtask);
        }
    }

    @Override
    public void updateSubtask(int id, Subtask subtask) {   // обновление сабтаска
        if (!isSubTaskExists(id) || !isEpicExists(subtask.getEpicId())) {
            return;
        }
        subtask.setId(id);
        if (!isTaskOverlaps(subtask)) {
            subtasks.put(id, subtask);
            Epic epic = epics.get(subtask.getEpicId());
            epic.setStatus(calculateEpicStatus(subtask.getEpicId()));
            epics.put(epic.getId(), epic);
            calculateDurationAndTimeForEpic(subtask.getEpicId());
            prioritizedTasks.add(subtask);
        }
    }

    @Override
    public Subtask getSubtask(int subtaskId) {    // получение сабтаска по идентификатору
        if (!isSubTaskExists(subtaskId)) {
            return null;
        }
        Subtask subtask = subtasks.get(subtaskId);
        printSubtask(subtask);   // печатаем таск
        inMemoryHistoryManager.add(subtask);  // добавляем таск в историю просмотров
        return subtask;
    }

    @Override
    public void printSubtask(Subtask subtask) { // печать  сабтаска
        System.out.println("ID подзадачи: " + subtask.getId() + ", Название: " + subtask.getName() + ", Описание: " +
                subtask.getDescription() + ", Cтатус: " + subtask.getStatus() + ", ID Эпика подзадачи: " + subtask.getEpicId() +
                ", Продолжительность: " + subtask.getDuration().toHours() +
                " часов " + subtask.getDuration().toMinutesPart() + " минут, " +
                "Дата и время начала: " + subtask.getStartTime() + ", Дата и время окончания: " + subtask.getEndTime());
    }

    @Override
    public Map<Integer, Subtask> getAllSubtasks() {  // получение списка всех сабтасков
        for (Subtask subtask : subtasks.values()) {
            printSubtask(subtask);
        }
        return subtasks;
    }

    @Override
    public void deleteSubtask(int subtaskId) {    // удаление сабтаска и удаление его id из эпика
        if (!isSubTaskExists(subtaskId)) {
            return;
        }
        Subtask subtask = subtasks.get(subtaskId);
        prioritizedTasks.remove(subtask);
        Epic epic = epics.get(subtask.getEpicId());
        subtasks.remove(subtaskId);
        epic.getSubtaskIds().remove(epic.getSubtaskIds().indexOf(subtaskId));
        epics.put(subtask.getEpicId(), epic);
        epic = epics.get(subtask.getEpicId());
        epic.setStatus(calculateEpicStatus(subtask.getEpicId()));
        epics.put(subtask.getEpicId(), epic);
        inMemoryHistoryManager.removeTaskFromHistory(subtaskId); // вызов метода удаление таска из истории просмотра
        calculateDurationAndTimeForEpic(subtask.getEpicId());
    }

    @Override
    public void deleteAllSubtasks() {     // удаление всех сабтасков и удаление их айди из эпиков
        for (Subtask subtask : subtasks.values()) {
            Epic epic = epics.get(subtask.getEpicId());
            epic.getSubtaskIds().remove(subtask.getId());
            epics.put(epic.getId(), epic);
            calculateDurationAndTimeForEpic(subtask.getEpicId());
            inMemoryHistoryManager.removeTaskFromHistory(subtask.getId()); // вызов метода удаление таска из истории просмотра
            prioritizedTasks.remove(subtasks.get(subtask.getId()));
        }
        subtasks.clear();
        System.out.println("Все сабтаски удалены");
    }

    @Override
    public Map<Integer, Subtask> getAllSubtasksFromEpic(int epicId) { // получение всех сабтасков определенного эпика
        Map<Integer, Subtask> subtasksOfEpic = new HashMap<>();
        Epic epic = epics.get(epicId);
        for (int subtaskId : epic.getSubtaskIds()) {
            Subtask subtask = subtasks.get(subtaskId);
            printSubtask(subtask);
            subtasksOfEpic.put(subtaskId, subtask);
        }
        return subtasksOfEpic;
    }

    @Override
    public void newEpic(Epic epic) { // создание нового эпика
        if (epic.getId() == null) {
            epic.setId(idCounter);
            idCounter++;
        }
        epics.put(epic.getId(), epic);
        calculateDurationAndTimeForEpic(epic.getId());
    }

    @Override
    public void updateEpic(int id, Epic epic) { // обновление эпика
        if (!isEpicExists(id)) {
            return;
        }
        epic.setId(id);
        epics.put(id, epic);
        calculateDurationAndTimeForEpic(epic.getId());
    }

    @Override
    public Epic getEpic(int epicId) { // получение эпика по идентификатору
        if (!isEpicExists(epicId)) {
            return null;
        }
        Epic epic = epics.get(epicId);
        printEpic(epic);            // печатаем эпик
        inMemoryHistoryManager.add(epic); // добавляем таск в историю просмотров
        return epic;
    }

    @Override
    public void printEpic(Epic epic) {      // печать эпика
        System.out.println("ID эпика: " + epic.getId() + ", Название: " + epic.getName() + ", Описание: " +
                epic.getDescription() + ", Cтатус: " + epic.getStatus() + ",  Продолжительность: " + epic.getDuration().toHours() +
                " часов " + epic.getDuration().toMinutesPart() + " минут, " +
                "Дата и время начала: " + epic.getStartTime() + ", Дата и время окончания: " + epic.getEndTime());
    }

    @Override
    public HashMap<Integer, Epic> getAllEpics() {  // получение списка всех эпиков
        for (Epic epic : epics.values()) {
            printEpic(epic);
        }
        return (HashMap<Integer, Epic>) epics;
    }

    @Override
    public void deleteEpic(int epicId) { // удаление эпика и всех его сабтасков
        if (!isEpicExists(epicId)) {
            return;
        }
        Epic epic = epics.get(epicId);
        for (int subtasksID : epic.getSubtaskIds()) {
            prioritizedTasks.remove(subtasks.get(subtasksID));
            subtasks.remove(subtasksID);
            inMemoryHistoryManager.removeTaskFromHistory(subtasksID); // вызов метода удаление таска из истории просмотра
        }
        epics.remove(epicId);
        inMemoryHistoryManager.removeTaskFromHistory(epicId); // вызов метода удаление таска из истории просмотра
    }

    @Override
    public void deleteAllEpics() {  // удаление всех эпиков и их сабтасков
        for (Epic epic : epics.values()) {
            for (int subtasksID : epic.getSubtaskIds()) {
                prioritizedTasks.remove(subtasks.get(subtasksID));
                subtasks.remove(subtasksID);
                inMemoryHistoryManager.removeTaskFromHistory(subtasksID); // вызов метода удаление таска из истории просмотра
            }
        }
        epics.clear();
        System.out.println("Все эпики удалены");
    }


    @Override
    public Set<Task> printPrioritizedTasks() {   // вызов списка отсортированных по приоритету тасков
        System.out.println("Список задач, отсортированных по важности:\n");
        for (Task task : prioritizedTasks) {
            if (task instanceof Subtask) {
                printSubtask((Subtask) task);
            } else {
                printTask(task);
            }
        }
        return prioritizedTasks;
    }


    @Override
    public List<Task> printWatchedHistory() {    // печать списка просмотренных задач
        System.out.println("Последние просмотренные задачи:");
        List<Task> list = inMemoryHistoryManager.getHistory();
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
        return list;
    }

    private boolean isTaskOverlaps(Task taskToCheck) {   // метод проверки имеются ли пересекающиеся задачи
        for (Task task : prioritizedTasks) {
            if (!taskToCheck.getStartTime().isAfter(task.getEndTime()) && !task.getStartTime().isAfter(taskToCheck.getEndTime()) && !(Objects.equals(task.getId(), taskToCheck.getId()))) {
                System.out.println("Задача c ID: " + taskToCheck.getId() + " не создана. На это время уже существует задача c ID: " + task.getId());
                return true;
            }
        }
        return false;
    }

    private void addSubTaskToEpic(int subtaskId, int epicId) {   // добавление сабтаска к эпику
        Epic epicToUpdate = epics.get(epicId);
        epicToUpdate.getSubtaskIds().add(subtaskId);
        epicToUpdate.setStatus(calculateEpicStatus(epicId));
        epics.put(epicId, epicToUpdate);
        calculateDurationAndTimeForEpic(epicId);
    }

    protected StatusType calculateEpicStatus(int epicId) { // проверка статуса сабтасков и возврат нового статуса эпика
        Epic epicToCheck = epics.get(epicId);
        int subtasksDone = 0;
        int subtasksNew = 0;
        StatusType statusToSet;

        for (Integer subtaskId : epicToCheck.getSubtaskIds()) {
            if (checkSubtaskStatus(subtaskId) == StatusType.DONE) {
                subtasksDone++;
            } else if (checkSubtaskStatus(subtaskId) == StatusType.NEW) {
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

    private void calculateDurationAndTimeForEpic(int epicId) {   // расчет продолжительности и времени начала-конца
        Duration duration = Duration.ZERO;
        LocalDateTime startTime = LocalDateTime.MAX;
        LocalDateTime endTime = LocalDateTime.MIN;

        Epic epicToCalculate = epics.get(epicId);
        for (Integer subtaskId : epicToCalculate.getSubtaskIds()) {
            Subtask subtask = subtasks.get(subtaskId);
            duration = duration.plus(subtask.getDuration());
            if (subtask.getStartTime().isBefore(startTime)) {
                startTime = subtask.getStartTime();
            }
            if (subtask.getEndTime().isAfter(endTime)) {
                endTime = subtask.getEndTime();
            }
        }
        epicToCalculate.setDuration(duration);
        epicToCalculate.setStartTime(startTime);
        epicToCalculate.setEndTime(endTime);

        epics.put(epicId, epicToCalculate);
    }

    private boolean isEpicExists(int epicId) {   // проверка существует ли эпик
        boolean isEpicExists = false;
        if (epics.containsKey(epicId)) {
            isEpicExists = true;
        } else {
            System.out.println("Эпик с введенным ID отсутствует");
        }
        return isEpicExists;
    }

    private boolean isSubTaskExists(int subtaskId) {  // проверка существует ли сабтаск
        boolean isSubTaskExists = false;
        if (subtasks.containsKey(subtaskId)) {
            isSubTaskExists = true;
        } else {
            System.out.println("Подзадача с введенным ID отсутствует");
        }
        return isSubTaskExists;
    }

    private boolean isTaskExists(int taskId) {  // проверка существует ли таск
        boolean isTaskExists = false;
        if (tasks.containsKey(taskId)) {
            isTaskExists = true;
        } else {
            System.out.println("Задача с введенным ID отсутствует");
        }
        return isTaskExists;
    }

    private StatusType checkSubtaskStatus(int subtaskId) {  // проверка статуса сабтаска
        Subtask subtask = subtasks.get(subtaskId);
        return subtask.getStatus();
    }
}
