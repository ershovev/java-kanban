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
    private File file;
    private final int MINIMUM_TASK_STRING_LENGTH = 5;
    private final String HEADER_FOR_TASKS_AND_HISTORY_FILE = "id,type,name,status,description,epic,duration,startDateAndTime\n";
    private final String TASK = "task";
    private final String SUBTASK = "subtask";
    private final String EPIC = "epic";

    public FileBackedTasksManager(File file) {
        this.file = file;
    }


    public void loadFromFile() throws ManagerSaveException, ManagerReadTaskException {
        try {
            String tasksAndHistory = Files.readString(file.toPath());   //читаем файл
            int idCounterToRestore = -1;  // переменная для восстановления счетчика id тасков

            String[] splittedHeaderTasksAndHistory = tasksAndHistory.split("\n");   // разбиваем по строкам
            String[] splittedTasksAndHistory = Arrays.copyOfRange(splittedHeaderTasksAndHistory, 1, splittedHeaderTasksAndHistory.length); // удаляем первую строку
            for (String string : splittedTasksAndHistory) {
                String[] stringSplit = string.split(",");     // разбиваем строки и в зависимости от типа вызываем метод создания таска
                if (stringSplit.length >= MINIMUM_TASK_STRING_LENGTH) {
                    switch (stringSplit[1]) {
                        case "TASK":
                            super.newTask(taskFromString(stringSplit));
                            if (idCounterToRestore < taskFromString(stringSplit).getId()) {
                                idCounterToRestore = taskFromString(stringSplit).getId() + 1;
                            }
                            break;
                        case "EPIC":
                            super.newEpic(epicFromString(stringSplit));
                            if (idCounterToRestore < epicFromString(stringSplit).getId()) {
                                idCounterToRestore = epicFromString(stringSplit).getId() + 1;
                            }
                            break;
                        case "SUBTASK":
                            super.newSubTask(subtaskFromString(stringSplit));
                            if (idCounterToRestore < subtaskFromString(stringSplit).getId()) {
                                idCounterToRestore = subtaskFromString(stringSplit).getId() + 1;
                            }
                            break;
                        default:
                            try {
                                int x = parseInt(stringSplit[1]);
                            } catch (NumberFormatException e) {
                                super.deleteAllTasks();
                                super.deleteAllEpics();
                                throw new ManagerReadTaskException("Неизвестный тип задачи:" + stringSplit[1] + " Задачи не сохранены");
                            }
                            break;
                    }
                }
            }
            idCounter = idCounterToRestore;

            String lastString = splittedTasksAndHistory[splittedTasksAndHistory.length - 1];     // берем последнюю строку которая содержит историю просмотров
            String[] historyTasks = lastString.split(",");
            if (!TASK.equals(historyTasks[1]) && !SUBTASK.equals(historyTasks[1]) && !EPIC.equals(historyTasks[1])) {
                List<Integer> history = historyFromString(lastString);  // вызываем метод который возвращает лист с id тасков
                for (Integer i : history) {     // проходимся по листу и добавляем таски в историю
                    if (tasks.containsKey(i)) {
                        inMemoryHistoryManager.add(tasks.get(i));
                    } else if (subtasks.containsKey(i)) {
                        inMemoryHistoryManager.add(subtasks.get(i));
                    } else {
                        inMemoryHistoryManager.add(epics.get(i));
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
    public void deleteTask(int taskID) {
        try {
            super.deleteTask(taskID);
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Task getTask(int taskID) {
        Task task = null;
        try {
            task = super.getTask(taskID);
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

    private List<Integer> historyFromString(String value) {   // метод обработки строки и возврата листа с id тасков истории
        List<Integer> history = new ArrayList<>();
        String[] historyTasks = value.split(",");
        for (String string : historyTasks) {
            history.add(parseInt(string));
        }
        return history;
    }

    private void save() throws ManagerSaveException {
        try {
            Path path = file.toPath();
            Files.writeString(path, HEADER_FOR_TASKS_AND_HISTORY_FILE);   // добавляем первую строку

            for (var entry : tasks.entrySet()) {               // проходимся по таскам и добавляем их
                Task task = entry.getValue();
                Files.writeString(path, taskToString(task), StandardOpenOption.APPEND);
            }

            for (var entry : epics.entrySet()) {             // проходимся по эпика и добавляем их
                Epic epic = entry.getValue();
                Files.writeString(path, taskToString(epic), StandardOpenOption.APPEND);
            }

            for (var entry : subtasks.entrySet()) {          // проходимся по сабтаскам и добавялем их
                Subtask subtask = entry.getValue();
                Files.writeString(path, taskToString(subtask), StandardOpenOption.APPEND);
            }

            Files.writeString(path, historyToString(inMemoryHistoryManager), StandardOpenOption.APPEND); // добавляем строку с историей просмотров
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения/записи файла");
        }
    }

    private String taskToString(Task task) {   // создание строки из таска
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

    private String historyToString(HistoryManager manager) {  // создание строки из истории просмотров
        List<Task> tasks = manager.getHistory();
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        for (Task task : tasks) {
            if (sb.length() > 1) {
                sb.append(",").append(task.getId());
            } else {
                sb.append(task.getId());
            }
        }
        return sb.toString();
    }

    private Task taskFromString(String[] value) {     // создание таска из строки
        Task task = new Task(value[2], value[4], StatusType.valueOf(value[3]), parseInt(value[5]), value[6]);
        task.setId(Integer.parseInt(value[0]));
        return task;
    }

    private Epic epicFromString(String[] value) {    // создание эпика из строки
        Epic epic = new Epic(value[2], value[4], StatusType.valueOf(value[3]));
        epic.setId(Integer.parseInt(value[0]));
        return epic;
    }

    private Subtask subtaskFromString(String[] value) {  // создание сабтаска из строки
        Subtask subtask = new Subtask(value[2], value[4], StatusType.valueOf(value[3]), parseInt(value[5]), parseInt(value[6]), value[7]);
        subtask.setId(Integer.parseInt(value[0]));
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
