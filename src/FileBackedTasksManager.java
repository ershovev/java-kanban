import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

public class FileBackedTasksManager extends InMemoryTaskManager {
    File file;

    protected FileBackedTasksManager(File file) {
        this.file = file;
    }


    public void loadFromFile() throws ManagerSaveException {
        try {
            String tasksAndHistory = Files.readString(file.toPath());   //читаем файл
            int idCounterToRestore = -1;  // переменная для восстановления счетчика id тасков

            String[] split = tasksAndHistory.split("\n");   // разбиваем по строкам
            for (String string : split) {
                String[] split2 = string.split(",");     // разбиваем строки и в зависимости от типа вызываем метод создания таска
                if (split2.length > 4) {
                    switch (split2[1]) {
                        case "TASK":
                            super.newTask(taskFromString(split2));
                            if (idCounterToRestore < taskFromString(split2).id) {
                                idCounterToRestore = taskFromString(split2).id + 1;
                            }
                            break;
                        case "EPIC":
                            super.newEpic(epicFromString(split2));
                            if (idCounterToRestore < epicFromString(split2).id) {
                                idCounterToRestore = epicFromString(split2).id + 1;
                            }
                            break;
                        case "SUBTASK":
                            super.newSubTask(subtaskFromString(split2));
                            if (idCounterToRestore < subtaskFromString(split2).id) {
                                idCounterToRestore = subtaskFromString(split2).id + 1;
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
            idCounter = idCounterToRestore;

            String lastString = split[split.length - 1];     // берем последнюю строку которая содержит историю просмотров
            String[] historyTasks = lastString.split(",");
            if (!historyTasks[1].equals("type") && !historyTasks[1].equals("TASK") && !historyTasks[1].equals("EPIC") && !historyTasks[1].equals("SUBTASK")) {
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

    public List<Integer> historyFromString(String value) {   // метод обработки строки и возврата листа с id тасков истории
        List<Integer> history = new ArrayList<>();
        String[] historyTasks = value.split(",");
        for (String string : historyTasks) {
            history.add(parseInt(string));
        }
        return history;
    }

    public void save() throws ManagerSaveException {
        try {
            Path path = file.toPath();
            Files.writeString(path, "id,type,name,status,description,epic\n");   // добавляем первую строку

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

    public String taskToString(Task task) {   // создание строки из таска
        String taskString;
        if (task instanceof Subtask) {
            taskString = task.id + "," + "SUBTASK" + "," + task.name + "," + task.status + "," + task.description + "," +
                    ((Subtask) task).epicID + "," + task.duration.toMinutes() + "," + task.startTime + "\n";
        } else if (task instanceof Epic) {
            taskString = task.id + "," + "EPIC" + "," + task.name + "," + task.status + "," + task.description + "\n";
        } else {
            taskString = task.id + "," + "TASK" + "," + task.name + "," + task.status + "," + task.description
                    + "," + task.duration.toMinutes() + "," + task.startTime + "\n";
        }

        return taskString;
    }

    public String historyToString(HistoryManager manager) {  // создание строки из истории просмотров
        ArrayList<Task> tasks = manager.getHistory();
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        for (Task task : tasks) {
            if (sb.length() > 1) {
                sb.append("," + task.id);
            } else {
                sb.append(task.id);
            }
        }
        return sb.toString();
    }

    public Task taskFromString(String[] value) {     // создание таска из строки
        Task task = new Task(value[2], value[4], StatusType.valueOf(value[3]), parseInt(value[5]), value[6]);
        task.id = Integer.parseInt(value[0]);
        return task;
    }

    public Epic epicFromString(String[] value) {    // создание эпика из строки
        Epic epic = new Epic(value[2], value[4], StatusType.valueOf(value[3]));
        epic.id = Integer.parseInt(value[0]);
        return epic;
    }

    public Subtask subtaskFromString(String[] value) {  // создание сабтаска из строки
        Subtask subtask = new Subtask(value[2], value[4], StatusType.valueOf(value[3]), parseInt(value[5]), parseInt(value[6]), value[7]);
        subtask.id = Integer.parseInt(value[0]);
        return subtask;
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
    public Subtask getSubtask(int subtaskID) {
        Subtask subtask = null;
        try {
            subtask = super.getSubtask(subtaskID);
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
        return subtask;
    }


    @Override
    public void deleteSubtask(int subtaskID) {
        try {
            super.deleteSubtask(subtaskID);
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
    public Epic getEpic(int epicID) {
        Epic epic = null;
        try {
            epic = super.getEpic(epicID);
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
        return epic;
    }

    @Override
    public void deleteEpic(int epicID) {
        try {
            super.deleteEpic(epicID);
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

    class ManagerSaveException extends Exception {
        public ManagerSaveException(final String message) {
            super(message);
        }
    }
}
