package com.example.vaadinapp.component;

import com.example.vaadinapp.domain.User;
import com.example.vaadinapp.domain.UserDto;
import com.example.vaadinapp.view.UserList;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;


@SpringComponent
@UIScope
public class UserEditor extends VerticalLayout implements KeyNotifier {

    @Setter
    private String filterText = "";

    private User user;

    private TextField name = new TextField("", "name");
    private TextField surname = new TextField("", "surname");
    private TextField country = new TextField("", "country");
    // Create a DateField with the default style
    private DatePicker dateOfBirth = new DatePicker();
    private IntegerField height = new IntegerField("", "height");
    private NumberField weight = new NumberField("", "weight");

    private Button save = new Button("Save");
    private Button cancel = new Button("Cancel");
    private Button delete = new Button("Delete");
    private HorizontalLayout buttons = new HorizontalLayout(save, cancel, delete);

    private Binder<User> binder = new Binder<>(User.class);
    @Setter
    private ChangeHandler changeHandler;
    private boolean isPost = true;

    public interface ChangeHandler {
        void onChange();
    }

    @Autowired
    public UserEditor() {
        height.setMin(10);
        weight.setValue(20.0);
        weight.setMin(20);

        add(surname, name, country, dateOfBirth, height, weight, buttons);

        binder.bindInstanceFields(this);

        setSpacing(true);

        save.getElement().getThemeList().add("primary");
        delete.getElement().getThemeList().add("error");

        addKeyPressListener(Key.ENTER, e -> save());

        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> editUser(user));
        setVisible(false);
    }

    private void save() {
        WebClient client = WebClient.create("http://pamiwserver-env.eba-2bpp63gm.eu-central-1.elasticbeanstalk.com");
        if (isPost) {
            client.post()
                    .uri(builder -> builder.path("competitor").build())
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(
                            Mono.just(new UserDto(user.getName(), user.getSurname(), user.getCountry(), user.getDateOfBirth(), user.getHeight(), user.getWeight())),
                            UserDto.class)
                    .retrieve()
                    .toBodilessEntity().block();
            changeHandler.onChange();
        } else {
            client.put()
                    .uri("/competitor/" + user.getId())
                    .body(Mono.just(new UserDto(user.getName(), user.getSurname(), user.getCountry(), user.getDateOfBirth(), user.getHeight(), user.getWeight())),
                            UserDto.class)
                    .retrieve().bodyToMono(Void.class).block();
            changeHandler.onChange();
        }
    }

    private void delete() {
        WebClient client = WebClient.create("http://pamiwserver-env.eba-2bpp63gm.eu-central-1.elasticbeanstalk.com");
        client.delete()
                .uri("/competitor/" + user.getId())
                .retrieve()
                .bodyToMono(Void.class).block();
        changeHandler.onChange();
    }

    public void editUser(User u) {
        if (u == null) {
            setVisible(false);
            return;
        }

        if (u.getId() != null) {
            isPost = false;
            this.user = u;
        } else {
            this.user = u;
            this.user.setHeight(10);
            this.user.setWeight(10);
        }

        binder.setBean(this.user);

        setVisible(true);

        surname.focus();
    }
}
