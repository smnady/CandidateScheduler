package ru.smnadya.app.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.smnadya.app.constants.EventFilterType;
import ru.smnadya.app.constants.EventType;
import ru.smnadya.app.dao.CandidateDAO;
import ru.smnadya.app.dao.EventDAO;
import ru.smnadya.app.models.Candidate;
import ru.smnadya.app.models.Event;
import ru.smnadya.app.utils.EventFilter;
import ru.smnadya.app.utils.ScheduleSerializer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;
import static org.springframework.format.annotation.DateTimeFormat.ISO.TIME;

@Controller
@RequestMapping()
public class EventController {
    private final EventDAO eventDAO;
    private final CandidateDAO candidateDAO;

    @Autowired
    public EventController(EventDAO eventDAO,
                           CandidateDAO candidateDAO) {
        this.eventDAO = eventDAO;
        this.candidateDAO = candidateDAO;
    }

    @GetMapping("/events")
    public String getAllEvents(@RequestParam(value = "filterType", required = false)
                                   EventFilterType type,
                               @RequestParam(value = "filterDate", required = false)
                                       LocalDate filterDate,
                               HttpSession session,
                               Model model) {
        Long candidateId = (Long) session.getAttribute("candidateId");

        if (candidateId == null)
            return "redirect:/";

        List<Event> events = getEventsForView(candidateId, type, filterDate);
        model.addAttribute("eventsList", events);
        model.addAttribute("newEvent", new Event());
        type = type == null ? EventFilterType.ALL : type;
        model.addAttribute("filterType", type);

        return "schedule";
    }
    @PostMapping("create-event")
    public String createEvent(@RequestParam("eventType") EventType eventType,
                              @RequestParam("eventDate") @DateTimeFormat(iso = DATE) LocalDate eventDate,
                              @RequestParam(value = "eventTime", required = false)
                                  @DateTimeFormat(iso = TIME) LocalTime eventTime,
                              @RequestParam(value = "eventDescription", required = false)
                                  String eventDescription,
                              HttpSession session,
                              RedirectAttributes attributes) {
        Long candidateId = (Long) session.getAttribute("candidateId");
        if (candidateId == null) {
            attributes.addFlashAttribute("error",
                    "Ваша сессия истекла, выполните повторный вход");
            return "redirect:/";
        }
        Candidate candidate = candidateDAO.getCandidate(candidateId);
        Event event = new Event(eventDescription, eventDate,
                eventTime, eventType, candidate);
        eventDAO.addEvent(event);
        return "redirect:/events";
    }
    @PostMapping("delete-event")
    public String deleteEvent(@RequestParam("eventId") long eventId) {
        eventDAO.deleteEvent(eventId);
        return "redirect:/events";
    }
    @PostMapping("edit-event")
    public String changeEvent(@RequestParam("eventId") Long eventId,
                              Model model) {
        model.addAttribute("event", eventDAO.getEvent(eventId));
        return "editEvent";
    }
    @PostMapping("save-changed-event")
    public String saveChangedEvent(@RequestParam("id") long id,
                                   @RequestParam("eventDate") @DateTimeFormat(iso = DATE)
                                   LocalDate eventDate,
                                   @RequestParam(value = "eventTime", required = false)
                                       @DateTimeFormat(iso = TIME) LocalTime eventTime,
                                   @RequestParam("description") String description) {
        Event event = new Event(description, eventDate, eventTime);
        eventDAO.updateEvent(id, event);
        return "redirect:/events";
    }
    @GetMapping("download-schedule")
    public String downloadSchedule(@RequestParam(value = "filterType", required = false)
                                       EventFilterType type,
                                   @RequestParam("eventsList") List<Long> eventIds,
                                   HttpSession session,
                                   Model model) {
        if (session.getAttribute("candidateId") == null)
            return "redirect:/";
        List<Event> events = eventIds.stream()
                .map(eventDAO::getEvent).collect(Collectors.toList());
        model.addAttribute("eventsList", events);
        model.addAttribute("filterType", type);
        return "pageForPDF";
    }
    @GetMapping("export-schedule")
    public ResponseEntity<byte[]> serializeSchedule(HttpSession session) {
        Long candidateId = (Long) session.getAttribute("candidateId");
        ScheduleSerializer serializer = new ScheduleSerializer();
        List<Event> events = eventDAO.getEventsBy(candidateId);
        try {
            File file = new File("schedule.json");
            serializer.saveEvents(events, file);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=schedule.json");
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

            byte[] fileBytes = Files.readAllBytes(file.toPath());

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(file.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(fileBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private List<Event> getEventsForView(long candidateId, EventFilterType type,
                                         LocalDate filterDate) {
        List<Event> events = eventDAO.getEventsBy(candidateId);
        EventFilter filter = type == null ?
                new EventFilter(EventFilterType.ALL) : new EventFilter(type);
        return filterDate == null ? filter.getFilteredList(events, LocalDate.now())
                : filter.getFilteredList(events, filterDate);
    }
}
