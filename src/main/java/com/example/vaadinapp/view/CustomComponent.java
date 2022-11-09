package com.example.vaadinapp.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasText;
import com.vaadin.flow.component.HtmlComponent;
import com.vaadin.flow.component.dependency.JavaScript;

import java.util.ListIterator;


public class CustomComponent extends Component implements HasText {
    HtmlComponent htmlComponent = new HtmlComponent("tag");

}
