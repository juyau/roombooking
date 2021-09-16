package org.thebreak.roombooking.app.controller;


import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping(value = "api/v1/app/test")
class TestController2 {

    @GetMapping()
    public String getString(){
        return "test ok";
    }
}
