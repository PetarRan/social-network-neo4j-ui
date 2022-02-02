package com.petarran.application.views.finddabblers;


import com.petarran.application.data.User;
import com.petarran.application.feign_client.FollowsFeignClient;
import com.petarran.application.feign_client.UserFeignClient;
import com.petarran.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinServletService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@PageTitle("Find Dabblers")
@Route(value = "users/:samplePersonID?/:action?(edit)", layout = MainLayout.class)
@Uses(Icon.class)
public class FindDabblersView extends Div {

    private static final List<User> invitedPeople = new ArrayList<>();

    private static Grid<User> grid;
    private static Div hint;
    private final UserFeignClient userFeignClient;
    private final FollowsFeignClient followsFeignClient;

    public FindDabblersView(UserFeignClient userFeignClient, FollowsFeignClient followsFeignClient) {
        this.userFeignClient = userFeignClient;
        this.followsFeignClient = followsFeignClient;
        this.setupInvitationForm();
            this.setupGrid();
            this.refreshGrid();
    }
    private void setupInvitationForm() {
            Collection<User> usersList = new ArrayList<>();
            ComboBox<User> comboBox = new ComboBox<User>();
            String loggedUser = VaadinServletService.getCurrentServletRequest().getSession().getAttribute("email")
                .toString();
            userFeignClient.findAllUsers()
                    .stream()
                    .filter(e -> !e.getEmail().startsWith(loggedUser))
                    .forEach(usersList::add);
            comboBox.setItems(usersList);
            comboBox.setItemLabelGenerator(User::getEmail);

            Button button = new Button("Follow", VaadinIcon.PLUS.create());
            button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            button.addClickListener(e -> {
                User user = userFeignClient.findUserByMail(VaadinServletService.getCurrentServletRequest().getSession().getAttribute("email")
                        .toString());
                if(followsFeignClient.amIFollowing(comboBox.getValue().getEmail(), user.getEmail())){
                    followsFeignClient.followUser(user, comboBox.getValue().getEmail());
                    popUpNotification("User Followed!", NotificationVariant.LUMO_PRIMARY);
                }
                else{
                    popUpNotification("Already Following this user.", NotificationVariant.LUMO_PRIMARY);
                }



                comboBox.setValue(null);
            });

            HorizontalLayout layout = new HorizontalLayout(comboBox, button);
            layout.setFlexGrow(1, comboBox);
            layout.setSpacing(true);
            layout.setMargin(true);

            add(layout);

        addClassNames("find-dabblers-view", "flex", "flex-col", "h-full");


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
        hint.add(new Image("https://i.imgur.com/6HG0BlT.png", "Travel together."));
        hint.setSizeFull();

        add(hint, grid);
    }

    private void popUpNotification(String s, NotificationVariant lumo) {
        Notification notification = new Notification();
        notification.setDuration(3500);
        notification.setPosition(Notification.Position.TOP_CENTER);
        notification.addThemeVariants(lumo);
        notification.setText(s);
        notification.open();
    }

}
