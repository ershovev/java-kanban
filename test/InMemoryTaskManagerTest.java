import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.Managers;
import ru.yandex.practicum.taskmanager.InMemoryTaskManager;


import static org.junit.jupiter.api.Assertions.*;


class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void beforeEach() {
        Managers managers = new Managers();
        taskManager = managers.getInMemoryTaskManager();
    }

    @Test
    void idCounterShouldBeZero() {
        assertNotNull(taskManager.getIdCounter(), "Счётчик id отсутствует");
        assertEquals(0, taskManager.getIdCounter(), "Счётчик id не равен нулю");
    }

    @Test
    void tasksShouldBeZero() {
        assertNotNull(taskManager.getTasks(), "Хэшмапа для задач отсутствует");
        assertEquals(0, taskManager.getTasks().size(), "Размер хэшмапы для задач не равен нулю");
    }

    @Test
    void subtasksShouldBeZero() {
        assertNotNull(taskManager.getSubtasks(), "Хэшмапа для подзадач отсутствует");
        assertEquals(0, taskManager.getSubtasks().size(), "Размер хэшмапы для подзадач не равен нулю");
    }

    @Test
    void epicsShouldBeZero() {
        assertNotNull(taskManager.getEpics(), "Хэшмапа для эпиков отсутствует");
        assertEquals(0, taskManager.getEpics().size(), "Размер хэшмапы для эпиков не равен нулю");
    }

    @Test
    void prioritizedTasksShouldBeZero() {
        assertNotNull(taskManager.getPrioritizedTasks(), "Сет для приоритетных тасков отсутствует");
        assertEquals(0, taskManager.getPrioritizedTasks().size(), "Размер сета для приоритетных тасков не равен нулю");
    }
}




