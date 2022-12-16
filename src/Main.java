public class Main {

    public static void main(String[] args) {
        Managers managers = new Managers();

        TaskManager inMemoryTaskManager = managers.getDefault();

        inMemoryTaskManager.newTask(new Task("Задача 1", "Описание задачи 1", StatusType.NEW));
        inMemoryTaskManager.newTask(new Task("Задача 2", "Описание задачи 2", StatusType.NEW));

        inMemoryTaskManager.newEpic(new Epic("Эпик 1", "Описание эпика 1", StatusType.NEW));
        inMemoryTaskManager.newSubTask(new Subtask("Подзадача 1", "Описание подазадачи 1", StatusType.NEW, 2));
        inMemoryTaskManager.newSubTask(new Subtask("Подзадача 2", "Описание подазадачи 2", StatusType.NEW, 2));

        inMemoryTaskManager.newEpic(new Epic("Эпик 2", "Описание эпика 2", StatusType.NEW));
        inMemoryTaskManager.newSubTask(new Subtask("Подзадача 3", "Описание подазадачи 3", StatusType.NEW, 5));

        System.out.println("Просматриваем различные таски");
        inMemoryTaskManager.getTask(0);
        inMemoryTaskManager.getSubtask(4);
        inMemoryTaskManager.getEpic(2);
        inMemoryTaskManager.getSubtask(6);
        inMemoryTaskManager.getEpic(5);
        inMemoryTaskManager.getEpic(2);
        inMemoryTaskManager.getSubtask(6);
        inMemoryTaskManager.getEpic(5);
        inMemoryTaskManager.getEpic(2);
        inMemoryTaskManager.getSubtask(6);
        inMemoryTaskManager.getEpic(5);
        inMemoryTaskManager.getEpic(2);
        inMemoryTaskManager.getSubtask(6);
        inMemoryTaskManager.getEpic(5);

        System.out.println("Выводим историю просмотра");
        inMemoryTaskManager.printWatchedHistory(inMemoryTaskManager.getHistory());


        inMemoryTaskManager.getAllTasks();
        inMemoryTaskManager.getAllSubtasks();
        inMemoryTaskManager.getAllEpics();


        System.out.println("Обновляем подзадачу 2 и выводим ее и эпик");

        inMemoryTaskManager.updateSubtask(4, new Subtask("Обновленная подзадача 2", "Описание обонвленной подзадачи 2",
                StatusType.DONE, 2));
        inMemoryTaskManager.getSubtask(4);
        inMemoryTaskManager.getEpic(2);

        System.out.println("Удаляем подзадачу 2 и выводим ее эпик");
        inMemoryTaskManager.deleteSubtask(4);
        inMemoryTaskManager.getEpic(2);

        System.out.println("Обновляем подзадачу 3 и выводим ее и эпик");
        inMemoryTaskManager.updateSubtask(6, new Subtask("Обновленная подзадача 3", "Описание обновленной подзадачи 3",
                StatusType.DONE, 5));
        inMemoryTaskManager.getSubtask(6);
        inMemoryTaskManager.getEpic(5);

        System.out.println("Выводим все подзадачи");
        inMemoryTaskManager.getAllSubtasks();
        System.out.println("Удаляем эпик 1");
        inMemoryTaskManager.deleteEpic(2);
        System.out.println("Выводим все подзадачи снова");
        inMemoryTaskManager.getAllSubtasks();
    }
}
