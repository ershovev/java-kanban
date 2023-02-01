public class Managers {
    protected TaskManager getDefault() {
        return new InMemoryTaskManager();
    }
    protected HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
