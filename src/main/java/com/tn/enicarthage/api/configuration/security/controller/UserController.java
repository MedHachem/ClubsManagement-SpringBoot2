package com.tn.enicarthage.api.configuration.security.controller;

import com.tn.enicarthage.api.configuration.security.JWTUtil;
import com.tn.enicarthage.api.configuration.security.JwtUser;
import com.tn.enicarthage.api.configuration.security.user.RoleService;
import com.tn.enicarthage.api.configuration.security.user.User;
import com.tn.enicarthage.api.configuration.security.user.UserService;
import com.tn.enicarthage.api.exceptions.BindingErrorsResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


@CrossOrigin(origins = "http://localhost:64810")
@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    @Qualifier("jwtUserDetailsService")
    private UserDetailsService userDetailsService;
 @Autowired
 private RoleService roleService;

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public JwtUser getAuthenticatedUser(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader).substring(7);
        String username = jwtUtil.getUsernameFromToken(token);
        return (JwtUser) userDetailsService.loadUserByUsername(username);
    }



    @GetMapping("/AllUsers")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> allusers = userService.findAll();
        if (allusers == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else if (allusers.isEmpty())
            return new ResponseEntity<>(allusers, HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<>(allusers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return userService.findById(id)
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @PostMapping("/NewUser")
    public ResponseEntity<User> saveUser(@RequestBody @Valid User user, BindingResult bindingResult,
                                         UriComponentsBuilder uriComponentsBuilder) {
        BindingErrorsResponse errors = new BindingErrorsResponse();
        HttpHeaders headers = new HttpHeaders();
        if (bindingResult.hasErrors() || (user == null)) {
            errors.addAllErrors(bindingResult);
            headers.add("errors", errors.toJSON());
            return new ResponseEntity<>(headers, HttpStatus.BAD_REQUEST);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userService.save(user);
        headers.setLocation(uriComponentsBuilder.path("/users/{id}").buildAndExpand(user.getId()).toUri());
        return new ResponseEntity<>(user, headers, HttpStatus.CREATED);

    }

    @PutMapping("/Update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") Long id, @RequestBody @Valid User user,
                                           BindingResult bindingResult) {
        Optional<User> currentUser = userService.findById(id);
        BindingErrorsResponse errors = new BindingErrorsResponse();
        HttpHeaders headers = new HttpHeaders();
        if (bindingResult.hasErrors() || (user == null)) {
            errors.addAllErrors(bindingResult);
            headers.add("errors", errors.toJSON());
            return new ResponseEntity<>(headers, HttpStatus.BAD_REQUEST);
        }
        if (!currentUser.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        userService.update(user);
        return new ResponseEntity<>(user, HttpStatus.NO_CONTENT);
    }
    @DeleteMapping("/Delete/{id}")
    public ResponseEntity<User> deleteClub(@PathVariable("id") Long id) {
        Optional<User> userToDelete = userService.findById(id);
        if (!userToDelete.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        userService.delete(id);
        return new ResponseEntity<>(userToDelete.get(), HttpStatus.NO_CONTENT);
    }
    @GetMapping("/Count")
    public Long count()
    {
        return userService.count();
    }

}
