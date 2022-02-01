package com.petarran.application.views.personform;


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
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

@PageTitle("My Profile")
@Route(value = "my-profile", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@Uses(Icon.class)
public class MyProfile extends Div {

    private TextField firstName = new TextField("First name");
    private NumberField userId = new NumberField("User Id Number");
    private TextField lastName = new TextField("Last name");
    private EmailField email = new EmailField("Email address");
    private TextField imageUrl = new TextField("Image URL");
    private PhoneNumberField phone = new PhoneNumberField("Phone number");

    private Button cancel = new Button("Delete User Profile");
    private Button save = new Button("Save");

    private final UserFeignClient userFeignClient;


    public MyProfile(UserFeignClient userFeignClient) {
        this.userFeignClient = userFeignClient;
        addClassName("person-form-view");

        add(createTitle());
        add(createFormLayout());
        add(createButtonLayout());

        cancel.addClickListener(e -> {
            popUp("Are you sure you want to delete your profile?");
            userFeignClient.delete(userId.getValue().longValue());
            UI.getCurrent().getPage().reload();
        });
        save.addClickListener(e -> {

            if(this.firstName.getValue() == "" || this.lastName.getValue() == "" || this.email.getValue() == "" || this.imageUrl.getValue() == ""){

            }
            else{
            }
        });
    }

    private void popUp(String s) {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();
        Button yes = new Button("Yes", VaadinIcon.CHECK.create(), click -> {

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
        dialog.setHeight("250px");
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
        userId.setReadOnly(true);
        email.setRequiredIndicatorVisible(true);

        userFeignClient.find

        formLayout.add(firstName, lastName, phone, email, userId, imageUrl);
        return formLayout;
    }

    private Component createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save);
        buttonLayout.add(cancel);
        return buttonLayout;
    }

    private static class PhoneNumberField extends CustomField<String> {
        private ComboBox<String> countryCode = new ComboBox<>();
        private TextField number = new TextField();

        public PhoneNumberField(String label) {
            setLabel(label);
            countryCode.setWidth("120px");
            countryCode.setPlaceholder("Country");
            countryCode.setPattern("\\+\\d*");
            countryCode.setPreventInvalidInput(true);
            countryCode.setItems("+354", "+91", "+62", "+98", "+964", "+353", "+44", "+972", "+39", "+225");
            countryCode.addCustomValueSetListener(e -> countryCode.setValue(e.getDetail()));
            number.setPattern("\\d*");
            number.setPreventInvalidInput(true);
            HorizontalLayout layout = new HorizontalLayout(countryCode, number);
            layout.setFlexGrow(1.0, number);
            add(layout);
        }

        @Override
        protected String generateModelValue() {
            if (countryCode.getValue() != null && number.getValue() != null) {
                String s = countryCode.getValue() + " " + number.getValue();
                return s;
            }
            return "";
        }

        @Override
        protected void setPresentationValue(String phoneNumber) {
            String[] parts = phoneNumber != null ? phoneNumber.split(" ", 2) : new String[0];
            if (parts.length == 1) {
                countryCode.clear();
                number.setValue(parts[0]);
            } else if (parts.length == 2) {
                countryCode.setValue(parts[0]);
                number.setValue(parts[1]);
            } else {
                countryCode.clear();
                number.clear();
            }
        }
    }

}
