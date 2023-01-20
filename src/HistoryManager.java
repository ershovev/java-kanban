import java.util.List;

public interface HistoryManager {
    void add(Task task);

    List<Task> getHistory();

    void checkHistorySize();  // проверить размер и, если необходимо, удалить лишний элемент
}
