package ru.smnadya.app.schedule;

import org.springframework.stereotype.Service;
import ru.smnadya.app.event.Event;

import java.io.File;
import java.io.IOException;
import java.util.List;


@Service
public class ScheduleService {

    private final ScheduleSerializer serializer;

    public ScheduleService() {
        this.serializer = new ScheduleSerializer();
    }

    public File serializeEvents(List<Event> events) throws IOException {
        File file = new File("schedule.json");
        serializer.saveEvents(events, file);
        return file;
    }

}
