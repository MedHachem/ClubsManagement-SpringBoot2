package com.tn.enicarthage.api.service;

import com.tn.enicarthage.api.model.Membre;
import com.tn.enicarthage.api.repository.MembreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MembreService {
    private MembreRepository membreRepository;

    public MembreService() {
    }

    @Autowired
    public MembreService( MembreRepository membreRepository) {
        this.membreRepository = membreRepository;
    }


    public List<Membre> findAll (){
        return membreRepository.findAll();
    }

    public Optional<Membre> findById(Long id){
        return membreRepository.findById(id);
    }

    public Membre findByName(String name){
        return membreRepository.findByName(name);
    }

    public void save(Membre membre){
        membreRepository.save(membre);
    }

    public void update(Membre membre){
        membreRepository.save(membre);
    }

    public void delete(Long id){
        membreRepository.deleteById(id);
    }
    public long count() {
        return membreRepository.count();
    }
}
