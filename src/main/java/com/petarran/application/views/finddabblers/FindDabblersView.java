package com.petarran.application.views.finddabblers;


import com.petarran.application.data.User;
import com.petarran.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;

@PageTitle("Find Dabblers")
@Route(value = "users/:samplePersonID?/:action?(edit)", layout = MainLayout.class)
@Uses(Icon.class)
public class FindDabblersView extends Div {

    private static final List<User> invitedPeople = new ArrayList<>();

    private static Grid<User> grid;
    private static Div hint;

    public FindDabblersView() {
            this.setupInvitationForm();
            this.setupGrid();
            this.refreshGrid();
    }
    private void setupInvitationForm() {
            List<User> people = null;
            ComboBox<User> comboBox = new ComboBox<User>();
            comboBox.setItems(people);
            comboBox.setItemLabelGenerator(User::getEmail);

            Button button = new Button("Send invite");
            button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            button.addClickListener(e -> {
                //TODO sendInvitation(comboBox.getValue());
                comboBox.setValue(null);
            });

            HorizontalLayout layout = new HorizontalLayout(comboBox, button);
            layout.setFlexGrow(1, comboBox);

            add(layout);

        addClassNames("find-dabblers-view", "flex", "flex-col", "h-full");
        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        add(splitLayout);


    }

    private void refreshGrid() {
        if (invitedPeople.size() > 0) {
            grid.setVisible(true);
            hint.setVisible(false);
            grid.getDataProvider().refreshAll();
        } else {
            grid.setVisible(false);
            hint.setVisible(true);
        }
    }

    private void setupGrid() {
        grid = new Grid<>(User.class, false);
        grid.setAllRowsVisible(true);
        grid.addColumn(User::getFirstName).setHeader("Name");
        grid.addColumn(User::getLastName).setHeader("Last Name");
        grid.addColumn(User::getEmail).setHeader("Email");
        grid.addColumn(
                new ComponentRenderer<>(Button::new, (button, person) -> {
                    button.addThemeVariants(ButtonVariant.LUMO_ICON,
                            ButtonVariant.LUMO_ERROR,
                            ButtonVariant.LUMO_TERTIARY);
                    button.addClickListener(e -> {
                        //TODO this.removeInvitation(person)
                    });
                    button.setIcon(new Icon(VaadinIcon.BAN));
                })).setHeader("Unfollow");

        grid.setItems(invitedPeople);

        hint = new Div();
        hint.setText("No invitation has been sent");
        hint.getStyle().set("padding", "var(--lumo-size-l)")
                .set("text-align", "center").set("font-style", "italic")
                .set("color", "var(--lumo-contrast-70pct)");

        add(hint, grid);
    }

}
