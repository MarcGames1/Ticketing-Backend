package com.example.demo;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GreetingService {

    public String getGreeting(String lastName){
        return "Hello Hamza "+ lastName;
    }
}
