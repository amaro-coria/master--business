package com.master.demo.view;

import com.master.demo.dto.UserDTO;
import com.master.demo.persistence.User;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    @Value("${server.port}")
    private int port;

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView fetchUsers(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        modelAndView.addObject("users", getUsers());
        modelAndView.addObject("newUsr", new UserDTO());
        return modelAndView;
    }

    @RequestMapping(value = "/newUser", method = RequestMethod.POST)
    public ModelAndView addUser(@ModelAttribute UserDTO newUser){
        addUserToData(newUser);
        return fetchUsers();
    }

    private void addUserToData(UserDTO userDTO){
        RestTemplate restTemplate = new RestTemplate();
        String uri = new StringBuilder("http://localhost:").append(port).append("/rest/v1/user/users").toString();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", userDTO.getName());
        jsonObject.put("surname", userDTO.getSurname());
        jsonObject.put("address", userDTO.getAddress());
        jsonObject.put("zipCode", userDTO.getZipCode());
        jsonObject.put("id", "");
        HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);
        ResponseEntity<String> response = restTemplate.postForEntity(uri, entity, String.class);
        log.info("Result to the request: {}", response);
    }

    private List<User> getUsers(){
        try{
            RestTemplate restTemplatee = new RestTemplate();
            String uri = new StringBuilder("http://localhost:").append(port).append("/rest/v1/user/users").toString();
            ResponseEntity<List> response = restTemplatee.getForEntity(uri, List.class);
            if(response.getStatusCode().is2xxSuccessful()){
                return response.getBody();
            }
            log.info("No successful request to API: {}", response.getStatusCode());
            return new ArrayList<>();
        }catch (Exception e){
            log.error("Error fetching user data with message: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

}