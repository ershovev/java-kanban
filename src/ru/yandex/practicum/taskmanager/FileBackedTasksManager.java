package ru.yandex.practicum.taskmanager;

import ru.yandex.practicum.enums.StatusType;
import ru.yandex.practicum.historymanager.HistoryManager;
import ru.yandex.practicum.tasktypes.Epic;
import ru.yandex.practicum.tasktypes.Subtask;
import ru.yandex.practicum.tasktypes.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Integer.parseInt;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private File file = null;

    protected static final String HEADER_FOR_TASKS_AND_HISTORY_FILE = "id,type,name,status,description,epic,duration,startDateAndTime\n";
    private static final int MINIMUM_HISTORY_STRING_LENGTH = 2;
    private static final int INDEX_OF_SPACE_IN_HISTORY_STRING = 1;
    private static final int INDEX_OF_ID_IN_TASK_STRING = 0;
    private static final int INDEX_OF_NAME_IN_TASK_STRING = 2;
    private static final int INDEX_OF_STATUS_IN_TASK_STRING = 3;
    private static final int INDEX_OF_DESCRIPTION_IN_TASK_STRING = 4;
    private static final int INDEX_OF_DURATION_IN_TASK_STRING = 5;
    private static final int INDEX_OF_START_DATE_AND_TIME_IN_TASK_STRING = 6;
    private static final int INDEX_OF_EPICID_IN_SUBTASK_STRING = 5;
    private static final int INDEX_OF_DURATION_IN_SUBTASK_STRING = 6;
    private static final int INDEX_OF_START_DATE_AND_TIME_IN_SUBTASK_STRING = 7;


    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public FileBackedTasksManager() {
    }


    public void load() throws ManagerSaveException, ManagerReadTaskException {
        try {
            String tasksAndHistory = Files.readString(file.toPath());   //читаем файл
            int idCounterToRestore = -1;  // переменная для восстановления счетчика id тасков

            String[] splittedHeaderTasksAndHistory = tasksAndHistory.split("\n");   // разбиваем по строкам
            String[] splittedTasks = Arrays.copyOfRange(splittedHeaderTasksAndHistory, 1, splittedHeaderTasksAndHistory.length - 2); // удаляем первую и 2 последние строки (пустую и строку истории)

            for (String string : splittedTasks) {
                String[] fields = string.split(",");     // разбиваем строки и в зависимости от типа вызываем метод создания таска
                switch (fields[1]) {
                    case "TASK":
                        super.newTask(taskFromString(fields));
                        if (idCounterToRestore < taskFromString(fields).getId()) {
                            idCounterToRestore = taskFromString(fields).getId() + 1;
                        }
                        break;
                    case "EPIC":
                        super.newEpic(epicFromString(fields));
                        if (idCounterToRestore < epicFromString(fields).getId()) {
                            idCounterToRestore = epicFromString(fields).getId() + 1;
                        }
                        break;
                    case "SUBTASK":
                        super.newSubTask(subtaskFromString(fields));
                        if (idCounterToRestore < subtaskFromString(fields).getId()) {
                            idCounterToRestore = subtaskFromString(fields).getId() + 1;
                        }
                        break;
                    default:
                        super.deleteAllTasks();
                        super.deleteAllEpics();
                        throw new ManagerReadTaskException("Неизвестный тип задачи: " + fields[1] + ". Задачи не сохранены");
                }
            }
            setIdCounter(idCounterToRestore);

            String historyString = splittedHeaderTasksAndHistory[splittedHeaderTasksAndHistory.length - 1];     // берем последнюю строку которая содержит историю просмотров
            if (!(" ").equals(historyString)) {   // если historyString состоит из пробела, то историю не восстанавливаем
                List<Integer> history = historyFromString(historyString);  // вызываем метод, который возвращает лист с id тасков
                for (Integer i : history) {     // проходимся по листу и добавляем таски в историю
                    if (getTasks().containsKey(i)) {
                        inMemoryHistoryManager.add(getTasks().get(i));
                    } else if (getSubtasks().containsKey(i)) {
                        inMemoryHistoryManager.add(getSubtasks().get(i));
                    } else {
                        inMemoryHistoryManager.add(getEpics().get(i));
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения/записи файла");
        }
    }


    @Override
    public void newTask(Task task) {
        try {
            super.newTask(task);
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateTask(int id, Task task) {
        try {
            super.updateTask(id, task);
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteTask(int taskId) {
        try {
            super.deleteTask(taskId);
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Task getTask(int taskId) {
        Task task = null;
        try {
            task = super.getTask(taskId);
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
        return task;
    }

    @Override
    public void deleteAllTasks() {
        try {
            super.deleteAllTasks();
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void newSubTask(Subtask subtask) {
        try {
            super.newSubTask(subtask);
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateSubtask(int id, Subtask subtask) {
        try {
            super.updateSubtask(id, subtask);
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Subtask getSubtask(int subtaskId) {
        Subtask subtask = null;
        try {
            subtask = super.getSubtask(subtaskId);
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
        return subtask;
    }


    @Override
    public void deleteSubtask(int subtaskId) {
        try {
            super.deleteSubtask(subtaskId);
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }


    @Override
    public void deleteAllSubtasks() {
        try {
            super.deleteAllSubtasks();
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void newEpic(Epic epic) {
        try {
            super.newEpic(epic);
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateEpic(int id, Epic epic) {
        try {
            super.updateEpic(id, epic);
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Epic getEpic(int epicId) {
        Epic epic = null;
        try {
            epic = super.getEpic(epicId);
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
        return epic;
    }

    @Override
    public void deleteEpic(int epicId) {
        try {
            super.deleteEpic(epicId);
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteAllEpics() {
        try {
            super.deleteAllEpics();
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    protected List<Integer> historyFromString(String value) {   // метод обработки строки и возврата листа с id тасков истории
        List<Integer> history = new ArrayList<>();
        String[] historyTasks = value.split(",");
        for (String string : historyTasks) {
            history.add(parseInt(string));
        }
        return history;
    }

    protected void save() throws ManagerSaveException {
        try {
            Path path = file.toPath();
            Files.writeString(path, HEADER_FOR_TASKS_AND_HISTORY_FILE);   // добавляем первую строку

            for (var entry : getTasks().entrySet()) {               // проходимся по таскам и добавляем их
                Task task = entry.getValue();
                Files.writeString(path, taskToString(task), StandardOpenOption.APPEND);
            }

            for (var entry : getEpics().entrySet()) {             // проходимся по эпика и добавляем их
                Epic epic = entry.getValue();
                Files.writeString(path, taskToString(epic), StandardOpenOption.APPEND);
            }

            for (var entry : getSubtasks().entrySet()) {          // проходимся по сабтаскам и добавляем их
                Subtask subtask = entry.getValue();
                Files.writeString(path, taskToString(subtask), StandardOpenOption.APPEND);
            }

            Files.writeString(path, historyToString(inMemoryHistoryManager), StandardOpenOption.APPEND); // добавляем строку с историей просмотров
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения/записи файла");
        }
    }

    protected String taskToString(Task task) {   // создание строки из таска
        String taskString;
        if (task instanceof Subtask) {
            taskString = task.getId() + "," + "SUBTASK" + "," + task.getName() + "," + task.getStatus() + "," + task.getDescription() + "," +
                    ((Subtask) task).getEpicId() + "," + task.getDuration().toMinutes() + "," + task.getStartTime() + "\n";
        } else if (task instanceof Epic) {
            taskString = task.getId() + "," + "EPIC" + "," + task.getName() + "," + task.getStatus() + "," + task.getDescription() + "\n";
        } else {
            taskString = task.getId() + "," + "TASK" + "," + task.getName() + "," + task.getStatus() + "," + task.getDescription()
                    + "," + task.getDuration().toMinutes() + "," + task.getStartTime() + "\n";
        }

        return taskString;
    }

    protected String historyToString(HistoryManager manager) {  // создание строки из истории просмотров
        List<Task> tasks = manager.getHistory();
        StringBuilder sb = new StringBuilder();
        sb.append("\n ");   // добавляем пробел, чтобы корректно удалялись последние две строки когда хотим получить таски
        // при чтении из файла, и при этом история просмотров пуста
        for (Task task : tasks) {
            if (sb.length() > MINIMUM_HISTORY_STRING_LENGTH) {
                sb.append(",").append(task.getId());
            } else {
                sb.append(task.getId());
            }
        }
        if (sb.length() > MINIMUM_HISTORY_STRING_LENGTH) {         // если что-то из тасков добавилось в историю, то удаляем ранее добавленный пробел
            sb.deleteCharAt(INDEX_OF_SPACE_IN_HISTORY_STRING);
        }
        return sb.toString();
    }

    protected Task taskFromString(String[] value) {     // создание таска из строки
        Task task = new Task(value[INDEX_OF_NAME_IN_TASK_STRING], value[INDEX_OF_DESCRIPTION_IN_TASK_STRING], StatusType.valueOf(value[INDEX_OF_STATUS_IN_TASK_STRING]),
                parseInt(value[INDEX_OF_DURATION_IN_TASK_STRING]), value[INDEX_OF_START_DATE_AND_TIME_IN_TASK_STRING]);
        task.setId(Integer.parseInt(value[INDEX_OF_ID_IN_TASK_STRING]));
        return task;
    }

    protected Epic epicFromString(String[] value) {    // создание эпика из строки
        Epic epic = new Epic(value[INDEX_OF_NAME_IN_TASK_STRING], value[INDEX_OF_DESCRIPTION_IN_TASK_STRING], StatusType.valueOf(value[INDEX_OF_STATUS_IN_TASK_STRING]));
        epic.setId(Integer.parseInt(value[INDEX_OF_ID_IN_TASK_STRING]));
        return epic;
    }

    protected Subtask subtaskFromString(String[] value) {  // создание сабтаска из строки
        Subtask subtask = new Subtask(value[INDEX_OF_NAME_IN_TASK_STRING], value[INDEX_OF_DESCRIPTION_IN_TASK_STRING], StatusType.valueOf(value[INDEX_OF_STATUS_IN_TASK_STRING]),
                parseInt(value[INDEX_OF_EPICID_IN_SUBTASK_STRING]), parseInt(value[INDEX_OF_DURATION_IN_SUBTASK_STRING]), value[INDEX_OF_START_DATE_AND_TIME_IN_SUBTASK_STRING]);
        subtask.setId(Integer.parseInt(value[INDEX_OF_ID_IN_TASK_STRING]));
        return subtask;
    }

    public static class ManagerSaveException extends Exception {
        public ManagerSaveException(final String message) {
            super(message);
        }
    }

    public static class ManagerReadTaskException extends Exception {
        public ManagerReadTaskException(final String message) {
            super(message);
        }
    }
}
