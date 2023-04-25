package com.tn.enicarthage.api.service;

import com.tn.enicarthage.api.model.Event;
import com.tn.enicarthage.api.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EventService {
    private EventRepository eventRepository;

    public EventService() {
    }

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    public Optional<Event> findById(Long id) {
        return eventRepository.findById(id);
    }

    public Event findByTitle(String title) {
        return eventRepository.findByTitleAllIgnoreCase(title);
    }

    public long count() {
        return eventRepository.count();
    }
    public List<Event> findByClubsId(Long id) {
        return eventRepository.findByClubsId(id);
    }


    public void save(Event book) {
        eventRepository.save(book);
    }

    public void delete(Long id) {
        eventRepository.deleteById(id);
    }

    public void update(Event book) {
        eventRepository.save(book);
    }

}
