package com.tn.enicarthage.api.controller;


import com.tn.enicarthage.api.exceptions.BindingErrorsResponse;

import com.tn.enicarthage.api.model.Club;
import com.tn.enicarthage.api.service.ClubService;
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
@RequestMapping(value = "/Clubs")
public class ClubController {
    private ClubService clubService;

    public ClubController(ClubService clubService) {
        this.clubService = clubService;
    }

    @GetMapping("/All")
    public ResponseEntity<List<Club>> getAllClubs() {
        List<Club> allClubs = clubService.findAll();
        if (allClubs == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else if (allClubs.isEmpty())
            return new ResponseEntity<>(allClubs, HttpStatus.NO_CONTENT);
        else return new ResponseEntity<>(allClubs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Club> getClub(@PathVariable Long id) {
        return clubService
                .findById(id)
                .map(club -> new ResponseEntity<>(club, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @GetMapping("/Count")
    public Long count()
    {
        return clubService.count();
    }
    @PostMapping("/NewClub")
    public ResponseEntity<Club> saveClub(@RequestBody @Valid Club club, BindingResult bindingResult,
                                             UriComponentsBuilder uriComponentsBuilder) {
        BindingErrorsResponse errors = new BindingErrorsResponse();
        HttpHeaders headers = new HttpHeaders();
        if (bindingResult.hasErrors() || (club == null)) {
            errors.addAllErrors(bindingResult);
            headers.add("errors", errors.toJSON());
            return new ResponseEntity<>(headers, HttpStatus.BAD_REQUEST);
        }
        clubService.save(club);
        headers.setLocation(uriComponentsBuilder.path("/clubs/{id}").buildAndExpand(club.getId()).toUri());
        return new ResponseEntity<>(club, headers, HttpStatus.CREATED);
    }

    @PutMapping("/Update/{id}")
    public ResponseEntity<Club> updateClub(@PathVariable("id") Long id, @RequestBody @Valid Club club,
                                               BindingResult bindingResult) {
        Optional<Club> currentAuthor = clubService.findById(id);
        BindingErrorsResponse errors = new BindingErrorsResponse();
        HttpHeaders headers = new HttpHeaders();
        if (bindingResult.hasErrors() || (club == null)) {
            errors.addAllErrors(bindingResult);
            headers.add("errors", errors.toJSON());
            return new ResponseEntity<>(headers, HttpStatus.BAD_REQUEST);
        }
        if (!currentAuthor.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        clubService.update(club);
        return new ResponseEntity<>(club, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/Delete/{id}")
    public ResponseEntity<Club> deleteClub(@PathVariable("id") Long id) {
        Optional<Club> clubToDelete = clubService.findById(id);
        if (!clubToDelete.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        clubService.delete(id);
        return new ResponseEntity<>(clubToDelete.get(), HttpStatus.NO_CONTENT);
    }

}
