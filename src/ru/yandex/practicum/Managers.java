package ru.yandex.practicum;

import ru.yandex.practicum.historymanager.HistoryManager;
import ru.yandex.practicum.historymanager.InMemoryHistoryManager;
import ru.yandex.practicum.taskmanager.FileBackedTasksManager;
import ru.yandex.practicum.taskmanager.HttpTaskManager;
import ru.yandex.practicum.taskmanager.InMemoryTaskManager;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;

public class Managers {

    private final File file = Path.of(System.getProperty("user.dir") + "\\" + "tasks.csv").toFile();
    private final URI url = URI.create("http://localhost:8078");

    public HttpTaskManager getDefault(String key) throws IOException, InterruptedException {
        return new HttpTaskManager(url, key);
    }

    public InMemoryTaskManager getInMemoryTaskManager() {
        return new InMemoryTaskManager();
    }

    public HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public FileBackedTasksManager getFileBackedTasksManager() {
        return new FileBackedTasksManager(file);
    }
}
