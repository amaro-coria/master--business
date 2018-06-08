package com.master.demo.dto;

import lombok.Data;

import java.io.Serializable;


@Data
public class UserDTO implements Serializable {


    private String name;
    private String surname;
    private String address;
    private String zipCode;

}
