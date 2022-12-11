public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();

        manager.newTask("Задача 1", "Описание задачи 1");
        manager.newTask("Задача 2", "Описание задачи 2");

        manager.newEpic("Эпик 1", "Описание эпика 1");
        manager.newSubTask("Подзадача 1", "Описание подазадачи 1", 2);
        manager.newSubTask("Подзадача 2", "Описание подазадачи 2", 2);

        manager.newEpic("Эпик 2", "Описание эпика 2");
        manager.newSubTask("Подзадача 3", "Описание подзадачи 3", 5);

        manager.getAllTasks();
        manager.getAllSubtasks();
        manager.getAllEpics();

        System.out.println("Обновляем подзадачу 2 и выводим ее и эпик");
        Subtask subtask = new Subtask(4,"Обновленная подзадача 2", "Описание обонвленной подзадачи 2",
                "DONE", 2);
        manager.updateSubtask(subtask);
        manager.getSubtask(4);
        manager.getEpic(2);

        System.out.println("Удаляем подзадачу 2 и выводим ее эпик");
        manager.deleteSubtask(4);
        manager.getEpic(2);

        System.out.println("Обновляем подзадачу 3 и выводим ее и эпик");
        subtask = new Subtask(6, "Обновленная подзадача 3", "Описание обновленной подзадачи 3",
                "DONE", 5);
        manager.updateSubtask(subtask);
        manager.getSubtask(6);
        manager.getEpic(5);

        System.out.println("Выводим все подзадачи");
        manager.getAllSubtasks();
        System.out.println("Удаляем эпик 1");
        manager.deleteEpic(2);
        System.out.println("Выводим все подзадачи снова");
        manager.getAllSubtasks();
    }
}
