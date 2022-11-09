package com.example.vaadinapp.component;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class HistoryRegister {
    @Getter
    private final List<String> events = new ArrayList<>();

    public void add(String event){
        events.add(event);
    }
}
