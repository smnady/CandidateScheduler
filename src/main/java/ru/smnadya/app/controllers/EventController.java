package ru.smnadya.app.controllers;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.smnadya.app.candidate.Candidate;
import ru.smnadya.app.candidate.CandidateService;
import ru.smnadya.app.event.Event;
import ru.smnadya.app.event.EventService;
import ru.smnadya.app.event.util.EventFilterType;
import ru.smnadya.app.event.util.EventType;
import ru.smnadya.app.schedule.ScheduleService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;
import static org.springframework.format.annotation.DateTimeFormat.ISO.TIME;
import static ru.smnadya.app.controllers.api.BaseControllerApiPath.BCAP_REDIRECT_HOME;
import static ru.smnadya.app.controllers.api.EventControllerApiPath.ECAP_CREATE_EVENT;
import static ru.smnadya.app.controllers.api.EventControllerApiPath.ECAP_DELETE_EVENT;
import static ru.smnadya.app.controllers.api.EventControllerApiPath.ECAP_DOWNLOAD_SCHEDULE;
import static ru.smnadya.app.controllers.api.EventControllerApiPath.ECAP_EDIT_EVENT;
import static ru.smnadya.app.controllers.api.EventControllerApiPath.ECAP_EVENTS;
import static ru.smnadya.app.controllers.api.EventControllerApiPath.ECAP_EXPORT_SCHEDULE;
import static ru.smnadya.app.controllers.api.EventControllerApiPath.ECAP_REDIRECT_EVENTS;
import static ru.smnadya.app.controllers.api.EventControllerApiPath.ECAP_SAVE_CHANGED_EVENT;

@Controller
@RequestMapping
public class EventController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventController.class);

    private final EventService eventService;
    private final ScheduleService scheduleService;
    private final CandidateService candidateService;

    @Autowired
    public EventController(EventService eventService, ScheduleService scheduleService, CandidateService candidateService) {
        this.eventService = eventService;
        this.scheduleService = scheduleService;
        this.candidateService = candidateService;
    }

    @GetMapping(ECAP_EVENTS)
    public String getAllEvents(@RequestParam(value = "filterType", required = false)
                               EventFilterType type,
                               @RequestParam(value = "filterDate", required = false)
                               LocalDate filterDate,
                               HttpSession session,
                               Model model) {
        Long candidateId = (Long) session.getAttribute("candidateId");
        if (candidateId == null) {
            LOGGER.error("Candidate with id={} not found", candidateId);
            return BCAP_REDIRECT_HOME;
        }

        Candidate candidate = candidateService.getCandidateById(candidateId);
        List<Event> events = eventService.getEventsForView(candidate, type, filterDate);
        model.addAttribute("eventsList", events);
        model.addAttribute("newEvent", new Event());
        type = type == null ? EventFilterType.ALL : type;
        model.addAttribute("filterType", type);

        return "schedule";
    }

    @PostMapping(ECAP_CREATE_EVENT)
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
            attributes.addFlashAttribute("error", "Ваша сессия истекла, выполните повторный вход");
            return BCAP_REDIRECT_HOME;
        }
        Candidate candidate = candidateService.getCandidateById(candidateId);

        Event event = new Event(eventDescription, eventDate, eventTime, eventType, null);
        eventService.createEvent(candidate, event);
        return ECAP_REDIRECT_EVENTS;
    }

    @PostMapping(ECAP_DELETE_EVENT)
    public String deleteEvent(@RequestParam("eventId") long eventId) {
        eventService.deleteEvent(eventId);
        return ECAP_REDIRECT_EVENTS;
    }

    @PostMapping(ECAP_EDIT_EVENT)
    public String changeEvent(@RequestParam("eventId") Long eventId,
                              Model model) {
        model.addAttribute("event", eventService.getEvent(eventId));
        return "editEvent";
    }

    @PostMapping(ECAP_SAVE_CHANGED_EVENT)
    public String saveChangedEvent(@RequestParam("id") long id,
                                   @RequestParam("eventDate") @DateTimeFormat(iso = DATE)
                                   LocalDate eventDate,
                                   @RequestParam(value = "eventTime", required = false)
                                   @DateTimeFormat(iso = TIME) LocalTime eventTime,
                                   @RequestParam("description") String description) {
        Event event = new Event(description, eventDate, eventTime);
        eventService.updateEvent(id, event);
        return ECAP_REDIRECT_EVENTS;
    }

    @GetMapping(ECAP_DOWNLOAD_SCHEDULE)
    public String downloadSchedule(@RequestParam(value = "filterType", required = false)
                                   EventFilterType type,
                                   @RequestParam("eventsList") List<Long> eventIds,
                                   HttpSession session,
                                   Model model) {
        if (session.getAttribute("candidateId") == null)
            return BCAP_REDIRECT_HOME;
        List<Event> events = eventIds.stream().map(eventService::getEvent).collect(Collectors.toList());
        model.addAttribute("eventsList", events);
        model.addAttribute("filterType", type);
        return "pageForPDF";
    }

    @GetMapping(ECAP_EXPORT_SCHEDULE)
    public ResponseEntity<byte[]> exportSchedule(HttpSession session) {
        Long candidateId = (Long) session.getAttribute("candidateId");
        if (candidateId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Candidate candidate = candidateService.getCandidateById(candidateId);
        List<Event> events = eventService.getEventsForView(candidate, null, null);

        try {
            File file = scheduleService.serializeEvents(events);

            byte[] fileBytes = Files.readAllBytes(file.toPath());
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=schedule.json");
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(file.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(fileBytes);
        } catch (IOException e) {
            LOGGER.error("Произошла ошибка при экспорте расписания {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
