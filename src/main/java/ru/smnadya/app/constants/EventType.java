package ru.smnadya.app.constants;

public enum EventType {
    DEBATE("Дебаты"),
    MEETING_WITH_VOTERS("Встреча с избирателями"),
    PRESS_CONFIDENTIALITY("Пресс-конференция"),
    ELABORATION_OF_THESES("Проработка тезисов"),
    MEETING_WITH_TEAM("Встреча с командой"),
    DEFAULT_EVENT("Мероприятие");

    private final String description;
    EventType(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
}
