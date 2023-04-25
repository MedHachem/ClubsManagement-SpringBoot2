package com.tn.enicarthage.api.controller;

import com.tn.enicarthage.api.exceptions.BindingErrorsResponse;
import com.tn.enicarthage.api.model.Event;
import com.tn.enicarthage.api.service.EventService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:64810")

@RestController
@RequestMapping(value = "/Events")
public class EventController {
    private EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/All")
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> allEvents = eventService.findAll();
        if (allEvents == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else if (allEvents.isEmpty())
            return new ResponseEntity<>(allEvents, HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<>(allEvents, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEvent(@PathVariable Long id) {
        return eventService.findById(id)
                .map(event -> new ResponseEntity<>(event, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }



    @GetMapping("/by-club-id/{clubIds}")
    public ResponseEntity<List<Event>> getAllEventsByClubId(@PathVariable Long[] clubIds) {
        List<Event> allEventsByClubId = new ArrayList<>();
        for (Long clubId : clubIds) {
            List<Event> eventsByClubId = eventService.findByClubsId(clubId);
            if (!eventsByClubId.isEmpty()) allEventsByClubId.addAll(eventsByClubId);
        }
        if (allEventsByClubId.isEmpty())
            return new ResponseEntity<>(allEventsByClubId, HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(allEventsByClubId, HttpStatus.OK);
    }
    @GetMapping("/Count")
    public Long count()
    {
        return eventService.count();
    }
    @PostMapping("/NewEvent")
    public ResponseEntity<Event> saveEvent(@RequestBody @Valid Event event, BindingResult bindingResult,
                                         UriComponentsBuilder uriComponentsBuilder) {
        BindingErrorsResponse errors = new BindingErrorsResponse();
        HttpHeaders headers = new HttpHeaders();
        if (bindingResult.hasErrors() || (event == null)) {
            errors.addAllErrors(bindingResult);
            headers.add("errors", errors.toJSON());
            return new ResponseEntity<>(headers, HttpStatus.BAD_REQUEST);
        }

        eventService.save(event);
        headers.setLocation(uriComponentsBuilder.path("/events/{id}").buildAndExpand(event.getId()).toUri());
        return new ResponseEntity<>(event, headers, HttpStatus.CREATED);

    }

    @PutMapping("Update/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable("id") Long id, @RequestBody @Valid Event event,
                                           BindingResult bindingResult) {
        Optional<Event> currentEvent = eventService.findById(id);
        BindingErrorsResponse errors = new BindingErrorsResponse();
        HttpHeaders headers = new HttpHeaders();
        if (bindingResult.hasErrors() || (event == null)) {
            errors.addAllErrors(bindingResult);
            headers.add("errors", errors.toJSON());
            return new ResponseEntity<>(headers, HttpStatus.BAD_REQUEST);
        }
        if (!currentEvent.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        eventService.update(event);
        return new ResponseEntity<>(event, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("Delete/{id}")
    public ResponseEntity<Event> deleteEvent(@PathVariable("id") Long id) {
        Optional<Event> eventToDelete = eventService.findById(id);
        if (!eventToDelete.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        eventService.delete(id);
        return new ResponseEntity<>(eventToDelete.get(), HttpStatus.NO_CONTENT);

    }
}
