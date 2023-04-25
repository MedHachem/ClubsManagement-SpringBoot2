package com.tn.enicarthage.api.repository;

import com.tn.enicarthage.api.model.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {

    Club findByNameAllIgnoreCase(String name);

}
