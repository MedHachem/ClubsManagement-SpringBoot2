package com.tn.enicarthage.api.repository;

import com.tn.enicarthage.api.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Event findByTitleAllIgnoreCase(String title);

    List<Event> findByClubsId(Long id);

    @Query("SELECT b " +
            "FROM Event b " +
            "JOIN FETCH b.clubs ")
    List<Event> findAll();

}
