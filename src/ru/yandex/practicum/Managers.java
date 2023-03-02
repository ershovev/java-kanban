package ru.yandex.practicum;

import ru.yandex.practicum.historymanager.HistoryManager;
import ru.yandex.practicum.historymanager.InMemoryHistoryManager;
import ru.yandex.practicum.taskmanager.FileBackedTasksManager;
import ru.yandex.practicum.taskmanager.InMemoryTaskManager;

import java.io.File;
import java.nio.file.Path;

public class Managers {

    private final File file = Path.of(System.getProperty("user.dir") + "\\" + "tasks.csv").toFile();

    public InMemoryTaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public FileBackedTasksManager getFileBackedTasksManager() {
        return new FileBackedTasksManager(file);
    }

}
