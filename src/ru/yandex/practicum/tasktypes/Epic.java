package ru.yandex.practicum.tasktypes;

import ru.yandex.practicum.enums.StatusType;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    public Epic(String name, String description, StatusType status) {
        super(name, description, status);
    }

    private List<Integer> subtaskIds = new ArrayList<>();   // список для хранения айдишников тасков которые относятся к эпику


    public List<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void setSubtaskIds(List<Integer> subtaskIds) {
        this.subtaskIds = subtaskIds;
    }
}
