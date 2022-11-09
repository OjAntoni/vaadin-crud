package com.example.vaadinapp.view;

import com.example.vaadinapp.component.UserEditor;
import com.example.vaadinapp.domain.User;
import com.example.vaadinapp.web.CompetitorClient;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.List;


@Route("/users")
public class UserList extends AppLayout {

    private final UserEditor userEditor;

    private Grid<User> userGrid = new Grid<>(User.class);
    private final TextField filter = new TextField();
    private final Button addNewButton = new Button("New user", VaadinIcon.PLUS.create());
    private final HorizontalLayout toolbar = new HorizontalLayout(filter, addNewButton);

    @Getter
    private String filterText = "";

    @Autowired
    public UserList( UserEditor userEditor) {
        createHeader();
        createDrawer();

        this.userEditor = userEditor;

        filter.setPlaceholder("Type to filter");
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(field -> {
            fillList(field.getValue());
            userEditor.setFilterText(field.getValue());
        });

        setContent(new VerticalLayout(toolbar, userGrid, userEditor));

        userGrid
                .asSingleSelect()
                .addValueChangeListener(e -> userEditor.editUser(e.getValue()));
        userGrid.setColumns("id","name", "surname", "country","dateOfBirth","height","weight");

        addNewButton.addClickListener(e -> userEditor.editUser(new User()));

        userEditor.setChangeHandler(() -> {
            userEditor.setVisible(false);
            fillList(filter.getValue());
        });

        fillList("");
    }

    public void fillList(String name) {
        WebClient client = WebClient.create("http://pamiwserver-env.eba-2bpp63gm.eu-central-1.elasticbeanstalk.com");
        Mono<List<User>> listMono;
        if (name.isEmpty()) {
            listMono = client.get()
                    .uri(builder -> builder.path("competitors").queryParam("perPage", 120).
                            queryParam("page", 0).build())
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<>() {
                    });
        } else {
            filterText = name;
            listMono = client.get()
                    .uri(builder -> builder.path("competitors").queryParam("perPage", 120).
                            queryParam("page", 0)
                            .queryParam("name", name)
                            .build())
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<>() {
                    });
        }
        System.out.println(listMono.block());
        userGrid.setItems(listMono.block());
    }

    private void createHeader() {
        H1 logo = new H1("Vaadin CRM");
        logo.addClassNames("text-l", "m-m");


        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo);

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo);
        header.setWidth("100%");
        header.addClassNames("py-0", "px-m");

        addToNavbar(header);

    }

    private void createDrawer() {

        addToDrawer(new VerticalLayout(
                new RouterLink("Dashboard", UserList.class),
                new RouterLink("Home", MainLayout.class)
        ));
    }
}
