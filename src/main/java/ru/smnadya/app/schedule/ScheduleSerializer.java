package ru.smnadya.app.schedule;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ru.smnadya.app.event.Event;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ScheduleSerializer {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT).registerModule(new JavaTimeModule());


    public void saveEvents(List<Event> events, File file) throws IOException {
        objectMapper.writeValue(file, events);
    }

    public List<Event> loadEvents(File file) throws IOException {
        return objectMapper.readValue(file, new TypeReference<>() {
        });
    }

}
