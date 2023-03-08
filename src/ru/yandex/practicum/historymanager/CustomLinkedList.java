package ru.yandex.practicum.historymanager;

import ru.yandex.practicum.tasktypes.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomLinkedList<T> {
    private Node<Task> head;
    private Node<Task> tail;

    private Map<Integer, Node<Task>> nodeMap = new HashMap<>(); // хэшмапа для нод

    public Map<Integer, Node<Task>> getNodeMap() {
        return nodeMap;
    }

    protected void linkLast(Task task) {       // добавление ноды в хвост
        Node<Task> oldTail = tail;
        Node<Task> newNode = new Node<>(task);
        nodeMap.put(task.getId(), newNode); // добавляем айди задачи и ноду в хэшмапу
        newNode.prev = oldTail;
        tail = newNode;
        if (oldTail == null)
            head = newNode;
        else
            oldTail.next = newNode;
    }

    protected void removeNode(Node<Task> node) {     // удаление ноды из линкедлиста
        if (node.next != null && node.prev != null) {
            node.next.prev = node.prev;
            node.prev.next = node.next;
        } else if (node.prev != null) {
            node.prev.next = null;
            tail = node.prev;
        } else if (node.next != null) {
            node.next.prev = null;
            head = node.next;
        } else {
            head = null;
        }
    }

    protected List<Task> getTasks() {           // проход по все нодам для вывода истории
        List<Task> tasks = new ArrayList<>();
        Node<Task> n = head;
        while (n != null) {
            tasks.add(n.data);
            n = n.next;
        }
        return tasks;
    }

    protected class Node<T> {
        protected Task data;
        protected Node<T> next;
        protected Node<T> prev;

        protected Node(Task data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }
}