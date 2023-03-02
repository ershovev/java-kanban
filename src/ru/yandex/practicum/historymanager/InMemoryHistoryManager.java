package ru.yandex.practicum.historymanager;

import ru.yandex.practicum.tasktypes.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {


    private CustomLinkedList<Task> customLinkedList = new CustomLinkedList<>();


    @Override
    public void add(Task task) {   // добавление таска в историю просмотров
        removeTaskFromHistory(task.getId());
        customLinkedList.linkLast(task);
    }

    @Override
    public ArrayList<Task> getHistory() { // получить список просмотренных задач
        return customLinkedList.getTasks();
    }

    @Override
    public void removeTaskFromHistory(int id) {    // удаление таска из истории просмотров
        if (customLinkedList.getNodeMap().containsKey(id)) {
            customLinkedList.removeNode(customLinkedList.getNodeMap().remove(id)); // вырезаем ноду из хэшмапы по айди и посылаем в removeNode для удаления
        }
    }
}
