/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.mi.agnos.ums.controller;

import hu.mi.agnos.user.converter.AgnosUserConverter;
import hu.mi.agnos.user.entity.dao.AgnosDAOUser;
import hu.mi.agnos.user.entity.dto.AgnosDTOUser;
import hu.mi.agnos.user.repository.AgnosUserPropertyRepository;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Optional;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author parisek
 */
@RestController
@RequestMapping("/aums")
public class AgnosUserController {

    
    private final Logger log = LoggerFactory.getLogger(AgnosUserController.class);

     @Autowired
    private AgnosUserPropertyRepository userRepo;

    public AgnosUserController() {
    }

    @GetMapping("/users")
    Collection<AgnosDTOUser> users() {
        return AgnosUserConverter.dao2dto(userRepo.findAll());
    }

    @GetMapping("/user/{name}")
    ResponseEntity<?> getUser(@PathVariable String name) {
        Optional<AgnosDTOUser> user = AgnosUserConverter.dao2dto(userRepo.findById(name));
        return user.map(response -> ResponseEntity.ok().body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/user")
    ResponseEntity<AgnosDTOUser> createUser(@Valid @RequestBody AgnosDTOUser user) throws URISyntaxException {
        log.info("Request to create AgnosUser: {}", user);
        AgnosDAOUser daoUser = AgnosUserConverter.dto2dao(user);
        Optional<AgnosDTOUser> result = AgnosUserConverter.dao2dto(userRepo.save(daoUser));
        return ResponseEntity.created(new URI("/api/user/" + result.get().getName()))
                .body(result.get());
    }

    @PutMapping("/user/{name}")
    ResponseEntity<AgnosDTOUser> updateUser(@Valid @RequestBody AgnosDTOUser user, @PathVariable String userName) {
        log.info("Request to update AgnosUser: {}", user.getName());
        AgnosDAOUser daoUser = AgnosUserConverter.dto2dao(user);
        Optional<AgnosDTOUser> result = AgnosUserConverter.dao2dto(userRepo.save(userName, daoUser));
        return ResponseEntity.ok().body(result.get());
    }

    @DeleteMapping("/user/{name}")
    public ResponseEntity<?> deleteUser(@PathVariable String userName) {
        log.info("Request to delete AgnosUser: {}", userName);
        userRepo.deleteById(userName);
        return ResponseEntity.ok().build();
    }
}
