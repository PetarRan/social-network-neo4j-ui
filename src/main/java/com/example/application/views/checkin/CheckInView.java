package com.example.application.views.checkin;

import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Check In")
@Route(value = "checkin", layout = MainLayout.class)
public class CheckInView extends VerticalLayout {

    public CheckInView() {
        setSpacing(false);

        H2 title = new H2("Currently Not Travelling");
        add(title);
        Button travelling = new Button(VaadinIcon.AIRPLANE.create());
        travelling.addThemeVariants(ButtonVariant.LUMO_ERROR);
        travelling.addThemeVariants(ButtonVariant.LUMO_LARGE);

        travelling.addClickListener(buttonClickEvent -> {
            title.setText("Currently Travelling!");
            travelling.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
            travelling.removeThemeVariants(ButtonVariant.LUMO_ERROR);
        });

        add(travelling);

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }

}
