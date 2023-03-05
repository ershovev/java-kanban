package ru.yandex.practicum.taskmanager;

import com.google.gson.Gson;
import ru.yandex.practicum.serverclient.KVTaskClient;
import ru.yandex.practicum.tasktypes.Epic;
import ru.yandex.practicum.tasktypes.Subtask;
import ru.yandex.practicum.tasktypes.Task;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {

    KVTaskClient kvTaskClient;
    Gson gson;
    String key;

    public HttpTaskManager(URI serverUrl, String key) throws IOException, InterruptedException {
        kvTaskClient = new KVTaskClient(serverUrl);
        gson = new Gson();
        this.key = key;
    }

    @Override
    public void load() throws ManagerSaveException, ManagerReadTaskException {
        try {
            String tasksAndHistory = kvTaskClient.load(key);
            int idCounterToRestore = -1;  // переменная для восстановления счетчика id тасков

            String[] splittedHeaderTasksAndHistory = tasksAndHistory.split("\\\\n");   // разбиваем по строкам
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

            String historyStringBeforeChop = splittedHeaderTasksAndHistory[splittedHeaderTasksAndHistory.length - 1];     // берем последнюю строку которая содержит историю просмотров
            String historyString = historyStringBeforeChop.substring(0, historyStringBeforeChop.length() - 1);
            if (!historyString.equals(" ")) {   // если historyString состоит из пробела, то историю не восстанавливаем
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
        } catch (IOException | InterruptedException e) {
            throw new ManagerSaveException("Ошибка чтения/записи данных");
        }
    }

    @Override
    protected void save() throws ManagerSaveException {
        try {
            StringBuilder sb = new StringBuilder();

            sb.append(HEADER_FOR_TASKS_AND_HISTORY_FILE); // добавляем первую строку

            for (var entry : getTasks().entrySet()) {               // проходимся по таскам и добавляем их
                Task task = entry.getValue();
                sb.append(taskToString(task));
            }

            for (var entry : getEpics().entrySet()) {             // проходимся по эпика и добавляем их
                Epic epic = entry.getValue();
                sb.append(taskToString(epic));
            }

            for (var entry : getSubtasks().entrySet()) {          // проходимся по сабтаскам и добавляем их
                Subtask subtask = entry.getValue();
                sb.append(taskToString(subtask));
            }

            sb.append(historyToString(inMemoryHistoryManager)); // добавляем строку с историей просмотров
            String data = sb.toString();
            String serializedData = gson.toJson(data);

            kvTaskClient.put(key, serializedData);
        } catch (IOException | InterruptedException e) {
            throw new ManagerSaveException("Ошибка чтения/записи данных");
        }
    }
}
