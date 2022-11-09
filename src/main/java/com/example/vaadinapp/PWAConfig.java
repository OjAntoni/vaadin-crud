package com.example.vaadinapp;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import org.springframework.stereotype.Component;


@PWA(
        name = "User CRUD",
        shortName = "Users"
)
public class PWAConfig implements AppShellConfigurator {

}
