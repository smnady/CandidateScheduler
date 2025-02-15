package ru.smnadya.app.event.util;

public enum EventFilterType {

    ALL("Запланированные мероприятия"),
    TODAY("Расписание на сегодня"),
    PAST("Прошедшие мероприятия"),
    BY_DATE("Расписание на выбранную дату");

    private final String description;

    EventFilterType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
