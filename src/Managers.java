import java.io.File;
import java.nio.file.Path;

public class Managers {

    File file = Path.of(System.getProperty("user.dir") + "\\" + "tasks.csv").toFile();

    protected InMemoryTaskManager getDefault() {
        return new InMemoryTaskManager();
    }
    protected HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
    protected FileBackedTasksManager getFileBackedTasksManager() {
        return new FileBackedTasksManager(file);
    }

}
