package com.petarran.application.views.checkin;

import com.petarran.application.data.User;
import com.petarran.application.feign_client.UserFeignClient;
import com.petarran.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinServletService;

@PageTitle("Check In")
@Route(value = "checkin", layout = MainLayout.class)
public class CheckInView extends VerticalLayout {

    private final UserFeignClient userFeignClient;

    public CheckInView(UserFeignClient userFeignClient) {
        this.userFeignClient = userFeignClient;
        H2 title = new H2();
        Button travelling = new Button();
        setSpacing(false);


        travelling.setIcon(VaadinIcon.AIRPLANE.create());;

        User user = userFeignClient.findUserByMail(VaadinServletService.getCurrentServletRequest()
                .getSession().getAttribute("email")
                .toString());
        user.setTravelling(false);
        userFeignClient.updateUser(user);
        checkTravelStatus(user, title, travelling);



        travelling.addClickListener(buttonClickEvent -> {
            changeTravelStatus(user, title, travelling);
        });

        add(title, travelling);

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }

    private void changeTravelStatus(User user, H2 title, Button travelling) {
        if(user.getTravelling()!=null){
            if(!user.getTravelling()){
                title.setText("Currently Travelling!");
                travelling.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
                travelling.removeThemeVariants(ButtonVariant.LUMO_ERROR);
                user.setTravelling(true);
                userFeignClient.updateUser(user);

            } else {
                title.setText("Currently Not Travelling");
                travelling.addThemeVariants(ButtonVariant.LUMO_ERROR);
                travelling.addThemeVariants(ButtonVariant.LUMO_LARGE);
                user.setTravelling(false);
                userFeignClient.updateUser(user);
            }
        } else {
            title.setText("Currently Not Travelling");
            travelling.addThemeVariants(ButtonVariant.LUMO_ERROR);
            travelling.addThemeVariants(ButtonVariant.LUMO_LARGE);
            user.setTravelling(false);
            userFeignClient.updateUser(user);
        }

    }

    private void checkTravelStatus(User user, H2 title, Button travelling) {
        if(!user.getTravelling()){
            title.setText("Currently Not Travelling");
            travelling.addThemeVariants(ButtonVariant.LUMO_ERROR);
            travelling.addThemeVariants(ButtonVariant.LUMO_LARGE);
        } else {
            title.setText("Currently Travelling!");
            travelling.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
            travelling.removeThemeVariants(ButtonVariant.LUMO_ERROR);
        }
    }


}
