package com.tn.enicarthage.api.service;


import com.tn.enicarthage.api.model.Club;

import com.tn.enicarthage.api.repository.ClubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class ClubService {

    private ClubRepository clubRepository;

    public ClubService() {
    }

    @Autowired
    public ClubService(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;
    }


    public List<Club> findAll (){
        return clubRepository.findAll();
    }

    public Optional<Club> findById(Long id){
        return clubRepository.findById(id);
    }

    public Club findByName(String name){
        return clubRepository.findByNameAllIgnoreCase(name);
    }

    public void save(Club club){
        clubRepository.save(club);
    }

    public void update(Club club){
        clubRepository.save(club);
    }

    public void delete(Long id){
        clubRepository.deleteById(id);
    }

    public long count() {
        return clubRepository.count();
    }
}
