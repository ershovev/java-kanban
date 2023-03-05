package ru.yandex.practicum.tasktypes;

import ru.yandex.practicum.enums.StatusType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {

    public Epic(String name, String description, StatusType status) {
        super(name, description, status);
    }

    private List<Integer> subtaskIds = new ArrayList<>();   // список для хранения айдишников тасков которые относятся к эпику


    public List<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        Epic epic = (Epic) o;

        return Objects.equals(getId(), epic.getId())
                && Objects.equals(getName(), epic.getName())
                && Objects.equals(getDescription(), epic.getDescription())
                && Objects.equals(getStatus(), epic.getStatus());
    }
}
