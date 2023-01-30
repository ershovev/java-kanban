import java.util.ArrayList;
import java.util.HashMap;

public class CustomLinkedList<T> {
    public Node<T> head;
    public Node<T> tail;
    public int size = 0;

    protected HashMap<Integer, Node> nodeHashMap = new HashMap<>(); // хэшмапа для нод


    void linkLast(Task task) {       // добавление ноды в хвост
        size++;
        CustomLinkedList.Node oldTail = tail;
        CustomLinkedList.Node newNode = new CustomLinkedList.Node(task);
        nodeHashMap.put(task.id, newNode); // добавляем айди задачи и ноду в хэшмапу
        newNode.prev = oldTail;
        tail = newNode;
        if (oldTail == null)
            head = newNode;
        else
            oldTail.next = newNode;
    }

    void removeNode(Node node) {     // удаление ноды из линкедлиста
        if (node.next != null  && node.prev != null)  {
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
        size--;
    }

    public ArrayList<Task> getTasks() {           // проход по все нодам для вывода истории
        ArrayList<Task> tasks = new ArrayList<>();
        CustomLinkedList.Node n = head;
        while (n != null) {
           tasks.add((Task) n.data);
           n = n.next;
        }
        return tasks;
    }


    class Node<T> {
        public Task data;
        public Node<T> next;
        public Node<T> prev;

        public Node(Task data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }


}