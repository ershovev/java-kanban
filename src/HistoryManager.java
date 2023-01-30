import java.util.ArrayList;

public interface HistoryManager {
    void add(Task task);

    ArrayList<Task> getHistory(); //получить список просмотренных задач

    void removeTaskFromHistory(int id);
}
