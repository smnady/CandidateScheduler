package ru.smnadya.app.constants;

public enum EventFilterType {
    ALL("Запланированные мероприятия"),
    TODAY("Рассписание на сегодня"),
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
