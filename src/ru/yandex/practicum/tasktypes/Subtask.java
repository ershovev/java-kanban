package ru.yandex.practicum.tasktypes;

import ru.yandex.practicum.enums.StatusType;

import java.util.Objects;

public class Subtask extends Task {


    private int epicId;

    public Subtask(String name, String description, StatusType status, int epicId, int duration, String startTime) {
        super(name, description, status, duration, startTime);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        Subtask subtask = (Subtask) o;

        return Objects.equals(getId(), subtask.getId())
                && Objects.equals(getName(), subtask.getName())
                && Objects.equals(getDescription(), subtask.getDescription())
                && Objects.equals(getStatus(), subtask.getStatus())
                && Objects.equals(getDuration(), subtask.getDuration())
                && Objects.equals(getStartTime(), subtask.getStartTime())
                && Objects.equals(getEndTime(), subtask.getEndTime())
                && Objects.equals(getEpicId(), subtask.getEpicId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription(), getStatus(), getDuration(), getStartTime(), getEndTime(), epicId);
    }
}
