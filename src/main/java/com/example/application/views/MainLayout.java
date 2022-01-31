package com.example.application.views;


import com.example.application.views.checkin.CheckInView;
import com.example.application.views.finddabblers.FindDabblersView;
import com.example.application.views.following.FollowingView;
import com.example.application.views.personform.MyProfile;
import com.example.application.views.personform.MakeThreadView;
import com.example.application.views.wall2.Wall2View;
import com.example.application.views.likedPosts.LikedPostsView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;




/**
 * The main view is a top-level placeholder for other views.
 */
@PWA(name = "dabbl", shortName = "dabbl", enableInstallPrompt = false)
@Theme(themeFolder = "dabbl")
@PageTitle("Main")
public class MainLayout extends AppLayout {

    /**
     * A simple navigation item component, based on ListItem element.
     */

    Dialog popUpDialog;
    Label userMail = new Label("");

    public static class MenuItemInfo extends ListItem {

        private final Class<? extends Component> view;

        public MenuItemInfo(String menuTitle, String iconClass, Class<? extends Component> view) {
            this.view = view;
            RouterLink link = new RouterLink();
            // Use Lumo classnames for various styling
            link.addClassNames("flex", "mx-s", "p-s", "relative", "text-secondary");
            link.setRoute(view);

            Span text = new Span(menuTitle);
            // Use Lumo classnames for various styling
            text.addClassNames("font-medium", "text-s");

            link.add(new LineAwesomeIcon(iconClass), text);
            add(link);
        }

        public Class<?> getView() {
            return view;
        }

        /**
         * Simple wrapper to create icons using LineAwesome iconset. See
         * https://icons8.com/line-awesome
         */
        @NpmPackage(value = "line-awesome", version = "1.3.0")
        public static class LineAwesomeIcon extends Span {
            public LineAwesomeIcon(String lineawesomeClassnames) {
                // Use Lumo classnames for suitable font size and margin
                addClassNames("me-s", "text-l");
                if (!lineawesomeClassnames.isEmpty()) {
                    addClassNames(lineawesomeClassnames);
                }
            }
        }

    }

    private H1 viewTitle;

    public MainLayout() {
        setPrimarySection(Section.DRAWER);
        addToNavbar(true, createHeaderContent());
        addToDrawer(createDrawerContent());

        popUp();
    }

    private Component createHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.addClassName("text-secondary");
        toggle.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        toggle.getElement().setAttribute("aria-label", "Menu toggle");

        viewTitle = new H1();
        viewTitle.addClassNames("m-0", "text-l");

        Header header = new Header(toggle, viewTitle);
        header.addClassNames("bg-base", "border-b", "border-contrast-10", "box-border", "flex", "h-xl", "items-center",
                "w-full");
        return header;
    }

    private Component createDrawerContent() {
        Image appName = new Image("images/logo-dabbl.png", "dabbl");
        appName.setWidth("150px");
        appName.addClassNames("flex", "items-center", "m-0", "px-m", "text-m");

        com.vaadin.flow.component.html.Section section = new com.vaadin.flow.component.html.Section(appName,
                createNavigation(), createFooter());
        section.addClassNames("flex", "flex-col", "items-stretch", "max-h-full", "min-h-full");
        return section;
    }

    private Nav createNavigation() {
        Nav nav = new Nav();
        nav.addClassNames("border-b", "border-contrast-10", "flex-grow", "overflow-auto");
        nav.getElement().setAttribute("aria-labelledby", "views");

        // Wrap the links in a list; improves accessibility
        UnorderedList list = new UnorderedList();
        list.addClassNames("list-none", "m-0", "p-0");
        nav.add(list);

        for (MenuItemInfo menuItem : createMenuItems()) {
            list.add(menuItem);

        }
        return nav;
    }

    private MenuItemInfo[] createMenuItems() {
        return new MenuItemInfo[]{ //

                new MenuItemInfo("My Profile", "la la-user", MyProfile.class),

                new MenuItemInfo("Shared Wall", "la la-list", Wall2View.class), //

                new MenuItemInfo("Liked Posts", "la la-heart-o", LikedPostsView.class), //

                new MenuItemInfo("Following", "la la-tag", FollowingView.class), //

                new MenuItemInfo("Find Dabblers", "la la-user-friends", FindDabblersView.class), //

                new MenuItemInfo("Make a Thread", "la la-plus", MakeThreadView.class), //

                new MenuItemInfo("Check In", "la la-location-arrow", CheckInView.class), //

        };
    }

    private Footer createFooter() {
        Footer layout = new Footer();
        layout.addClassNames("flex", "items-center", "my-s", "px-m", "py-xs");
        this.userMail.addClassName("text-secondary");

        layout.add(new Div(this.userMail), new Div(new Button("Logout", VaadinIcon.EXIT.create(), click -> {
            UI.getCurrent().navigate("Login");
            UI.getCurrent().getPage().reload();
            //TODO obrisi username pored logouta
        })));


        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }

    private void popUp() {
        popUpDialog = new Dialog();
        popUpDialog.setWidth("450px");
        popUpDialog.setHeight("250x");
        FormLayout formLayout = new FormLayout();
        TextField firstName = new TextField();
        firstName.setRequired(true);
        firstName.setLabel("First Name: ");

        TextField lastName = new TextField();
        lastName.setRequired(true);
        lastName.setLabel("Last Name: ");

        EmailField emailField= new EmailField();
        emailField.setRequiredIndicatorVisible(true);
        emailField.setLabel("EMail: ");

        TextField imageUrl = new TextField();
        imageUrl.setRequired(true);
        imageUrl.setLabel("ImageUrL: ");

        Checkbox checkbox = new Checkbox("I already have an account.");
        checkbox.addClickListener(click -> {
           if(checkbox.getValue()){
               imageUrl.setEnabled(false);
               firstName.setEnabled(false);
               lastName.setEnabled(false);
           }
           else{
               imageUrl.setEnabled(true);
               firstName.setEnabled(true);
               lastName.setEnabled(true);
           }
        });

        formLayout.add(emailField, firstName, lastName, imageUrl , checkbox);
        formLayout.setColspan(imageUrl, 2);
        Button login = new Button("Login", VaadinIcon.ARROW_RIGHT.create());

        login.addClickListener(loginClick -> {
            if(checkbox.getValue()){
                if(!emailField.isEmpty() && !emailField.isInvalid()){
                    popUpDialog.close();
                    UI.getCurrent().navigate("my-profile");
                    //TODO Klik na dugme kada je vec u Bazi

                    this.userMail.setText(emailField.getValue());
                }
            }
            else{
                if(!emailField.isEmpty() && !firstName.isEmpty() &&
                !lastName.isEmpty() && !imageUrl.isEmpty() && !emailField.isInvalid()){
                    popUpDialog.close();
                    UI.getCurrent().navigate("my-profile");
                    //TODO Klik na dugme kada NIJE u bazi
                    this.userMail.setText(emailField.getValue());

                }

            }

        });

        formLayout.add(login);
        formLayout.setColspan(login, 2);
        popUpDialog.setCloseOnEsc(false);
        popUpDialog.setCloseOnOutsideClick(false);
        popUpDialog.add(formLayout);
        popUpDialog.open();

    }
}
