package com.tn.enicarthage.api.controller;
import com.tn.enicarthage.api.exceptions.BindingErrorsResponse;
import com.tn.enicarthage.api.model.Membre;
import com.tn.enicarthage.api.service.MembreService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
@CrossOrigin(origins = "http://localhost:64810")

@RestController
@RequestMapping(value = "/Membres")
public class MembreController {
    private MembreService membreService;

    public MembreController(MembreService membreService) {
        this.membreService = membreService;
    }



    @GetMapping("/All")
    public ResponseEntity<List<Membre>> getAllMembres() {
        List<Membre> allMembres = membreService.findAll();
        if (allMembres == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else if (allMembres.isEmpty())
            return new ResponseEntity<>(allMembres, HttpStatus.NO_CONTENT);
        else return new ResponseEntity<>(allMembres, HttpStatus.OK);
    }
    @GetMapping("/Count")
    public Long count()
    {
        return membreService.count();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Membre> getMembre(@PathVariable Long id) {
        return membreService
                .findById(id)
                .map(mem -> new ResponseEntity<>(mem, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(value="/NewMembre", consumes={"application/json"})
    public ResponseEntity<Membre> saveMembre(@RequestBody @Valid Membre membre, BindingResult bindingResult,
                                         UriComponentsBuilder uriComponentsBuilder) {
        BindingErrorsResponse errors = new BindingErrorsResponse();
        HttpHeaders headers = new HttpHeaders();
        if (bindingResult.hasErrors() || (membre == null)) {
            errors.addAllErrors(bindingResult);
            headers.add("errors", errors.toJSON());
            return new ResponseEntity<>(headers, HttpStatus.BAD_REQUEST);
        }
        membreService.save(membre);
        headers.setLocation(uriComponentsBuilder.path("/Membres/{id}").buildAndExpand(membre.getId()).toUri());
        return new ResponseEntity<>(membre, headers, HttpStatus.CREATED);
    }


    @PutMapping("Update/{id}")
    public ResponseEntity<Membre> updateMembre(@PathVariable("id") Long id, @RequestBody @Valid Membre membre,
                                           BindingResult bindingResult) {
        Optional<Membre> currentMembre = membreService.findById(id);
        BindingErrorsResponse errors = new BindingErrorsResponse();
        HttpHeaders headers = new HttpHeaders();
        if (bindingResult.hasErrors() || (membre == null)) {
            errors.addAllErrors(bindingResult);
            headers.add("errors", errors.toJSON());
            return new ResponseEntity<>(headers, HttpStatus.BAD_REQUEST);
        }
        if (!currentMembre.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        membreService.update(membre);
        return new ResponseEntity<>(membre, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("Delete/{id}")
    public ResponseEntity<Membre> deleteMembre(@PathVariable("id") Long id) {
        Optional<Membre> MembreToDelete = membreService.findById(id);
        if (!MembreToDelete.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        membreService.delete(id);
        return new ResponseEntity<>(MembreToDelete.get(), HttpStatus.NO_CONTENT);
    }

}
