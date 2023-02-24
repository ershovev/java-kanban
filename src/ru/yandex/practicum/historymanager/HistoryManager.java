package ru.yandex.practicum.historymanager;

import ru.yandex.practicum.tasktypes.Task;

import java.util.ArrayList;
import java.util.List;

public interface HistoryManager {
    void add(Task task);

    List<Task> getHistory(); //получить список просмотренных задач

    void removeTaskFromHistory(int id);
}
