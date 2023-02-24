package ru.yandex.practicum.tasktypes;

import ru.yandex.practicum.enums.StatusType;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    public List<Integer> tasks = new ArrayList<>();   // список для хранения айдишников тасков которые относятся к эпику

    public Epic(String name, String description, StatusType status) {
        super(name, description, status);
    }
}
