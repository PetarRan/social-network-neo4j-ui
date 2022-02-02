package com.petarran.application.views.personform;


import com.petarran.application.data.User;
import com.petarran.application.feign_client.FollowsFeignClient;
import com.petarran.application.feign_client.UserFeignClient;
import com.petarran.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinServletService;

@PageTitle("My Profile")
@Route(value = "my-profile", layout = MainLayout.class)
@Uses(Icon.class)
public class MyProfile extends Div {

    private TextField firstName = new TextField("First name");
    private TextField lastName = new TextField("Last name");
    private EmailField email = new EmailField("Email address");
    private TextField imageUrl = new TextField("Image URL");
    private NumberField phone = new NumberField("Phone number");

    private Button cancel = new Button("Delete User Profile");
    private Button save = new Button("Save");

    Div content = new Div();

    private final UserFeignClient userFeignClient;
    private final FollowsFeignClient followsFeignClient;


    public MyProfile(UserFeignClient userFeignClient, FollowsFeignClient followsFeignClient) {
        this.userFeignClient = userFeignClient;
        this.followsFeignClient = followsFeignClient;
        addClassName("person-form-view");
        content.addClassName("wall2-view");

        add(createTitle());
        add(createFormLayout());
        add(createButtonLayout());
        add(new H3("Users that you follow:"), content);

        userFeignClient.findAllMyFollowers(VaadinServletService.getCurrentServletRequest().getSession().getAttribute("email")
                .toString()).forEach(user -> {
                    content.add(createCard(user));
        });

        cancel.addClickListener(e -> {
            popUp("Are you sure you want to delete your profile?");
        });
        save.addClickListener(e -> {
            if(!this.firstName.getValue().equals("") && !this.lastName.getValue().equals("") &&
                    !this.email.getValue().equals("")){
                User user = userFeignClient.findUserByMail(email.getValue());
                if(user != null){
                    if(phone.getValue()!=null){
                        double phoneTemp = 0;
                        phoneTemp = Math.floor(phone.getValue());
                        int phoneInt= (int) phoneTemp;
                        user.setPhoneNumber(Integer.toString(phoneInt));
                    }
                    user.setImageUrl(imageUrl.getValue());
                    popUpNotification(email.getValue() + " updated.", NotificationVariant.LUMO_SUCCESS);
                    userFeignClient.updateUser(user);

                }
            }
        });
    }

    private void popUp(String s) {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();
        Button yes = new Button("Yes", VaadinIcon.CHECK.create(), click -> {
            User userTemp = userFeignClient.findUserByMail(VaadinServletService.getCurrentServletRequest().getSession().getAttribute("email")
                    .toString());
            userFeignClient.removeUser(userTemp);
            UI.getCurrent().navigate("Login");
            UI.getCurrent().getPage().reload();
        });
        Button no = new Button("No", click -> {
            dialog.close();
        });
        yes.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        no.addThemeVariants(ButtonVariant.LUMO_ERROR);
        H3 header = new H3(s);
        formLayout.add(header, yes, no);
        formLayout.setColspan(header, 2);

        dialog.setWidth("500px");
        dialog.setHeight("450px");
        dialog.add(formLayout);
        dialog.open();
    }


    private Component createTitle() {
        return new H3("Personal information");
    }

    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();
        email.setErrorMessage("Please enter a valid email address");
        email.setReadOnly(true);
        firstName.setRequired(true);
        firstName.setReadOnly(true);
//        firstName.setRequiredError("First name must be filled in!");
        lastName.setRequired(true);
        lastName.setReadOnly(true);
        imageUrl.setRequired(true);
        email.setRequiredIndicatorVisible(true);

        //Set default values
        String emailFromSession = VaadinServletService.getCurrentServletRequest().getSession().getAttribute("email")
                .toString();
        email.setValue(emailFromSession);
        User user = userFeignClient.findUserByMail(email.getValue());
        firstName.setValue(user.getFirstName());
        lastName.setValue(user.getLastName());
        if(user.getImageUrl()!=null){
            imageUrl.setValue(user.getImageUrl());
        }
        if(user.getPhoneNumber()!=null && !user.getPhoneNumber().equals("")){
            phone.setValue(Double.parseDouble(user.getPhoneNumber()));
        }

        formLayout.add(firstName, lastName, phone, email, imageUrl);
        return formLayout;
    }

    private Component createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save);
        cancel.addThemeVariants(ButtonVariant.LUMO_ERROR);
        buttonLayout.add(cancel);
        return buttonLayout;
    }

    private void popUpNotification(String s, NotificationVariant lumo) {
        Notification notification = new Notification();
        notification.setDuration(3500);
        notification.setPosition(Notification.Position.TOP_CENTER);
        notification.addThemeVariants(lumo);
        notification.setText(s);
        notification.open();
    }

    private HorizontalLayout createCard(User user) {
        HorizontalLayout card = new HorizontalLayout();
        card.addClassName("card");
        card.setSpacing(false);
        card.getThemeList().add("spacing-s");

        Image image = new Image();
        image.setSrc(user.getImageUrl());
        image.setAlt("user image");
        VerticalLayout description = new VerticalLayout();
        description.addClassName("description");
        description.setSpacing(false);
        description.setPadding(false);

        HorizontalLayout header = new HorizontalLayout();
        header.addClassName("header");
        header.getThemeList().add("spacing-s");

        Span name = new Span(user.getEmail());

        Button unfollow = new Button("Unfollow", VaadinIcon.BAN.create(), click -> {
            User userMy = userFeignClient.findUserByMail(VaadinServletService.getCurrentServletRequest().getSession().getAttribute("email")
                    .toString());
            popUpDialog("Are you sure you want to unfollow this user?", userMy, user.getEmail());
        });
        header.setSpacing(false);
        header.add(name, unfollow);
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        if(VaadinServletService.getCurrentServletRequest().getSession().getAttribute("email")
                .toString().equals("admin@admin.com")){
            Button banButton = new Button("Ban This User", VaadinIcon.GAVEL.create(), click->{
               User userTemp2 = userFeignClient.findUserByMail(name.getText());
               userFeignClient.removeUser(userTemp2);
               popUpNotification("User " + name.getText() + " recieved the ban hammer.",
                       NotificationVariant.LUMO_PRIMARY);
               UI.getCurrent().navigate("Login");
               UI.getCurrent().navigate("my-profile");
            });
            header.add(banButton);
        }

        Span post = new Span("You are following this user.");
        post.addClassName("post");

        description.add(header);
        card.add(image, description);
        card.setMargin(true);
        return card;
    }

    private void popUpDialog(String s, User userMy, String email) {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();
        Button yes = new Button("Yes", VaadinIcon.CHECK.create());
        Button no = new Button("No", click -> {
            dialog.close();
        });
        yes.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        yes.addClickListener(click -> {
            followsFeignClient.unfollowUser(userMy, email);
            dialog.close();
            UI.getCurrent().navigate("Login");
            UI.getCurrent().navigate("my-profile");
        });
        no.addThemeVariants(ButtonVariant.LUMO_ERROR);
        H3 header = new H3(s);
        formLayout.add(header, yes, no);
        formLayout.setColspan(header, 2);

        dialog.setWidth("500px");
        dialog.setHeight("450px");
        dialog.add(formLayout);
        dialog.open();
    }


}
