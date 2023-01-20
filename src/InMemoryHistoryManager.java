import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    protected List<Task> watchedTasks = new ArrayList<>();

    @Override
    public void add(Task task) {
        watchedTasks.add(task);
        checkHistorySize();  //вызываем метод проверки размера истории просмотров
    }

    @Override
    public List<Task> getHistory() { //получить список просмотренных задач
        return watchedTasks;
    }

    @Override
    public void checkHistorySize() {
        if (watchedTasks.size() > 10) {
            watchedTasks.remove(0);
        }
    }
}
