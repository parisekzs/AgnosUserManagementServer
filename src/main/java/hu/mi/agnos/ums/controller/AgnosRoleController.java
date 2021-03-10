/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.mi.agnos.ums.controller;

import hu.mi.agnos.user.converter.AgnosRoleConverter;
import hu.mi.agnos.user.entity.dao.AgnosDAORole;
import hu.mi.agnos.user.entity.dto.AgnosDTORole;
import hu.mi.agnos.user.repository.AgnosRolePropertyRepository;
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
public class AgnosRoleController {

    
    private final Logger log = LoggerFactory.getLogger(AgnosRoleController.class);

     @Autowired
    private AgnosRolePropertyRepository roleRepo;

    public AgnosRoleController() {
    }

    @GetMapping("/roles")
    Collection<AgnosDTORole> roles() {
        return AgnosRoleConverter.dao2dto(roleRepo.findAll());
    }

    @GetMapping("/role/{name}")
    ResponseEntity<?> getRole(@PathVariable String name) {
        Optional<AgnosDTORole> role = AgnosRoleConverter.dao2dto(roleRepo.findById(name));
        return role.map(response -> ResponseEntity.ok().body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/role")
    ResponseEntity<AgnosDTORole> createRole(@Valid @RequestBody AgnosDTORole role) throws URISyntaxException {
        log.info("Request to create AgnosRole: {}", role);
        AgnosDAORole daoRole = AgnosRoleConverter.dto2dao(role);
        Optional<AgnosDTORole> result = AgnosRoleConverter.dao2dto(roleRepo.save(daoRole));
        return ResponseEntity.created(new URI("/api/role/" + result.get().getName()))
                .body(result.get());
    }

    @PutMapping("/role/{name}")
    ResponseEntity<AgnosDTORole> updateRole(@Valid @RequestBody AgnosDTORole role) {
        log.info("Request to update AgnosRole: {}", role);
        AgnosDAORole daoRole = AgnosRoleConverter.dto2dao(role);
        Optional<AgnosDTORole> result = AgnosRoleConverter.dao2dto(roleRepo.save(daoRole));
        return ResponseEntity.ok().body(result.get());
    }

    @DeleteMapping("/role/{name}")
    public ResponseEntity<?> deleteRole(@PathVariable String name) {
        log.info("Request to delete AgnosRole: {}", name);
        roleRepo.deleteById(name);
        return ResponseEntity.ok().build();
    }
}
