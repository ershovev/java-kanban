import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.Managers;
import ru.yandex.practicum.taskmanager.InMemoryTaskManager;


import static org.junit.jupiter.api.Assertions.*;


class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void beforeEach() {
        Managers managers = new Managers();
        taskManager = managers.getDefault();
    }

    @Test
    void idCounterShouldBeZero() {
        assertNotNull(taskManager.idCounter, "Счётчик id отсутствует");
        assertEquals(0, taskManager.idCounter, "Счётчик id не равен нулю");
    }

    @Test
    void tasksShouldBeZero() {
        assertNotNull(taskManager.tasks, "Хэшмапа для задач отсутствует");
        assertEquals(0, taskManager.tasks.size(), "Размер хэшмапы для задач не равен нулю");
    }

    @Test
    void subtasksShouldBeZero() {
        assertNotNull(taskManager.subtasks, "Хэшмапа для подзадач отсутствует");
        assertEquals(0, taskManager.subtasks.size(), "Размер хэшмапы для подзадач не равен нулю");
    }

    @Test
    void epicsShouldBeZero() {
        assertNotNull(taskManager.epics, "Хэшмапа для эпиков отсутствует");
        assertEquals(0, taskManager.epics.size(), "Размер хэшмапы для эпиков не равен нулю");
    }

    @Test
    void prioritizedTasksShouldBeZero() {
        assertNotNull(taskManager.prioritizedTasks, "Сет для приоритетных тасков отсутствует");
        assertEquals(0, taskManager.prioritizedTasks.size(), "Размер сета для приоритетных тасков не равен нулю");
    }
}




