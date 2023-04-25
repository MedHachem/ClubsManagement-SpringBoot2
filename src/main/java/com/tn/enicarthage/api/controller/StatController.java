package com.tn.enicarthage.api.controller;

import com.tn.enicarthage.api.configuration.security.user.UserService;
import com.tn.enicarthage.api.service.ClubService;
import com.tn.enicarthage.api.service.MembreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;


@CrossOrigin(origins = "http://localhost:64810")
@RestController
public class StatController {
    @Autowired
    private ClubService clubService;
    @Autowired
    private UserService userService;
    @Autowired
    private MembreService membreService;

    @GetMapping("/counts")
    public ArrayList<Long> getAllCounts(){
        ArrayList<Long> l=new ArrayList<Long>();
        l.add(userService.count());
        l.add((clubService.count()));
        l.add((membreService.count()));
        return l;

    }
}
