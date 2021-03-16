/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.mi.ums.controller;

import hu.mi.user.properties.converter.RoleConverter;
import hu.mi.user.properties.entity.Role;
import hu.mi.user.properties.model.RoleDTO;
import hu.mi.user.properties.repository.RoleRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author parisek
 */
@RestController
@RequestMapping("/aums")
public class RoleController {

    
    private final Logger log = LoggerFactory.getLogger(RoleController.class);

     @Autowired
    private RoleRepo roleRepo;

    public RoleController() {
    }

    @GetMapping("/roles")
    Collection<RoleDTO> roles() {
        return RoleConverter.dao2dto(roleRepo.findAll());
    }

    @GetMapping("/role/{name}")
    ResponseEntity<?> getRole(@PathVariable String name) {
        Optional<RoleDTO> role = RoleConverter.dao2dto(roleRepo.findById(name));
        return role.map(response -> ResponseEntity.ok().body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/role")
    ResponseEntity<RoleDTO> createRole(@Valid @RequestBody RoleDTO role) throws URISyntaxException {
        log.info("Request to create AgnosRole: {}", role);
        Role daoRole = RoleConverter.dto2dao(role);
        Optional<RoleDTO> result = RoleConverter.dao2dto(roleRepo.save(daoRole));
        return ResponseEntity.created(new URI("/api/role/" + result.get().getName()))
                .body(result.get());
    }

    @PutMapping("/role/{name}")
    ResponseEntity<RoleDTO> updateRole(@Valid @RequestBody RoleDTO role) {
        log.info("Request to update AgnosRole: {}", role);
        Role daoRole = RoleConverter.dto2dao(role);
        Optional<RoleDTO> result = RoleConverter.dao2dto(roleRepo.save(daoRole));
        return ResponseEntity.ok().body(result.get());
    }

    @DeleteMapping("/role/{name}")
    public ResponseEntity<?> deleteRole(@PathVariable String name) {
        log.info("Request to delete AgnosRole: {}", name);
        roleRepo.deleteById(name);
        return ResponseEntity.ok().build();
    }
}
