package com.master.demo.rest;


import com.master.demo.dto.UserDTO;
import com.master.demo.persistence.User;
import com.master.demo.persistence.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/rest/v1/user")
public class UserRestController {

    @Autowired
    private UserRepository userRepository;

    private static final Logger log = LoggerFactory.getLogger(UserRestController.class);


    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ResponseEntity<List<User>> findAllUsers(){
        List<User> users = null;
        try {
            users = userRepository.findAll();
        } catch (Exception e) {
            log.error("Error getting all users with message: {}", e.getMessage());
        }
        if(users == null || users.isEmpty()){
            return new ResponseEntity<>(users, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<User> addUser(@RequestBody UserDTO userDTO){
        User user = new User();
        if(userDTO.getZipCode() == null || userDTO.getZipCode().isEmpty() || userDTO.getZipCode().length() < 4){
            return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
        }
        if(userDTO.getName() == null || userDTO.getName().isEmpty() || userDTO.getName().length() < 4){
            return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
        }
        user.setAddress(userDTO.getAddress());
        user.setName(userDTO.getName());
        user.setSurname(userDTO.getSurname());
        user.setZipCode(userDTO.getZipCode());
        try {
            User userSaved = userRepository.save(user);
            return new ResponseEntity<>(userSaved, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error saving user in database with message: {}", e.getMessage());
            return new ResponseEntity<>(user, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}