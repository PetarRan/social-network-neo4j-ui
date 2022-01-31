package com.example.application.views.wall2;

import com.example.application.components.leafletmap.LeafletMap;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;

import java.util.Arrays;
import java.util.List;

@PageTitle("Shared Wall")
@Route(value = "wall2", layout = MainLayout.class)
public class Wall2View extends Div implements AfterNavigationObserver {

    Grid<Person> grid = new Grid<>();

    public Wall2View() {
        addClassName("wall2-view");
        setSizeFull();
        grid.setHeight("100%");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
        grid.addComponentColumn(person -> createCard(person));
        add(grid);
    }

    private HorizontalLayout createCard(Person person) {
        HorizontalLayout card = new HorizontalLayout();
        card.addClassName("card");
        card.setSpacing(false);
        card.getThemeList().add("spacing-s");

        Image image = new Image();
        image.setSrc(person.getImage());
        VerticalLayout description = new VerticalLayout();
        description.addClassName("description");
        description.setSpacing(false);
        description.setPadding(false);

        HorizontalLayout header = new HorizontalLayout();
        header.addClassName("header");
        header.setSpacing(false);
        header.getThemeList().add("spacing-s");

        Span name = new Span(person.getName());
        name.addClassName("name");
        Span travelStatus = new Span(person.isTravelStatus());
        travelStatus.addClassName("date");
        header.add(name, travelStatus);

        Span post = new Span(person.getPost());
        post.addClassName("post");

        HorizontalLayout actions = new HorizontalLayout();
        actions.addClassName("actions");
        actions.setSpacing(false);
        actions.getThemeList().add("spacing-s");

        Button likeIcon = new Button(VaadinIcon.HEART.create());
        likeIcon.addThemeVariants(ButtonVariant.LUMO_ERROR);
        Span likes = new Span(person.getLikes());
        likes.addClassName("likes");

        Button locationCheck = new Button(person.getLocation() ,VaadinIcon.LOCATION_ARROW_CIRCLE.create());
        locationCheck.addClickListener(click -> {
            locationPopUp(person.getLocation());
        });

        actions.add(likeIcon, likes, locationCheck);

        description.add(header, post, actions);
        card.add(image, description);
        return card;
    }

    private void locationPopUp(String location) {
        Dialog dialog = new Dialog();
        VerticalLayout verticalLayout = new VerticalLayout();
        LeafletMap map = new LeafletMap();
        map.setView(42.546245, 	1.601554, 10);
        map.setWidth("700px");
        map.setHeight("700px");

        dialog.setWidth("800px");
        dialog.setHeight("800px");
        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(true);

        verticalLayout.add(map);
        dialog.add(verticalLayout);
        dialog.open();
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {

        // Set some data when this view is displayed.
        List<Person> persons = Arrays.asList( //
                createPerson("https://randomuser.me/api/portraits/men/42.jpg", "John Smith", true,
                        "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document without relying on meaningful content (also called greeking).",
                        "1K", "Belgrade"),
                createPerson("https://randomuser.me/api/portraits/women/42.jpg", "Abagail Libbie", true,
                        "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document without relying on meaningful content (also called greeking).",
                        "1K", "Belgrade"),
                createPerson("https://randomuser.me/api/portraits/men/24.jpg", "Alberto Raya", false,

                        "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document without relying on meaningful content (also called greeking).",
                        "1K", "Belgrade"),
                createPerson("https://randomuser.me/api/portraits/women/24.jpg", "Emmy Elsner", true,

                        "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document without relying on meaningful content (also called greeking).",
                        "1K", "Belgrade"),
                createPerson("https://randomuser.me/api/portraits/men/76.jpg", "Alf Huncoot", false,

                        "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document without relying on meaningful content (also called greeking).",
                        "1K", "Belgrade"),
                createPerson("https://randomuser.me/api/portraits/women/76.jpg", "Lidmila Vilensky", false,

                        "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document without relying on meaningful content (also called greeking).",
                        "1K", "Belgrade"),
                createPerson("https://randomuser.me/api/portraits/men/94.jpg", "Jarrett Cawsey", false,
                        "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document without relying on meaningful content (also called greeking).",
                        "1K", "Belgrade"),
                createPerson("https://randomuser.me/api/portraits/women/94.jpg", "Tania Perfilyeva", true,

                        "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document without relying on meaningful content (also called greeking).",
                        "1K", "Belgrade"),
                createPerson("https://randomuser.me/api/portraits/men/16.jpg", "Ivan Polo", false,

                        "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document without relying on meaningful content (also called greeking).",
                        "1K", "Belgrade")

        );

        grid.setItems(persons);
    }

    private static Person createPerson(String image, String name, boolean travelStatus, String post, String likes, String location) {
        Person p = new Person();
        p.setImage(image);
        p.setName(name);
        p.setTravelStatus(travelStatus);
        p.setPost(post);
        p.setLikes(likes);
        p.setLocation(location);

        return p;
    }

}
