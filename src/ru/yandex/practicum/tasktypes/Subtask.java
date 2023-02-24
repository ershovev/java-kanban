package ru.yandex.practicum.tasktypes;

import ru.yandex.practicum.enums.StatusType;

public class Subtask extends Task {


    private int epicId;

    public Subtask(String name, String description, StatusType status, int epicId, int duration, String startTime) {
        super(name, description, status, duration, startTime);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }
}
