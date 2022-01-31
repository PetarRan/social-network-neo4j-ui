package com.example.application.views.finddabblers;

import com.example.application.data.entity.SamplePerson;
import com.example.application.data.service.SamplePersonService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.Locale;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.artur.helpers.CrudServiceDataProvider;

@PageTitle("Find Dabblers")
@Route(value = "users/:samplePersonID?/:action?(edit)", layout = MainLayout.class)
@Uses(Icon.class)
public class FindDabblersView extends Div implements BeforeEnterObserver {

    private final String SAMPLEPERSON_ID = "samplePersonID";
    private final String SAMPLEPERSON_EDIT_ROUTE_TEMPLATE = "users/%d/edit";

    private Grid<SamplePerson> grid = new Grid<>(SamplePerson.class, false);

    private TextField firstName;
    private TextField lastName;
    private TextField email;
    private TextField phone;
    private DatePicker dateOfBirth;
    private TextField occupation;
    private Checkbox important;

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Add", VaadinIcon.USER_CHECK.create());

    private BeanValidationBinder<SamplePerson> binder;

    private SamplePerson samplePerson;

    private SamplePersonService samplePersonService;

    private TextField filterText = new TextField();

    public FindDabblersView(@Autowired SamplePersonService samplePersonService) {
        this.samplePersonService = samplePersonService;

        // Configure Grid
        grid.addColumn(SamplePerson::getFirstName).setAutoWidth(true).setHeader("First Name");
        grid.addColumn(SamplePerson::getLastName).setAutoWidth(true).setHeader("Last Name");
        grid.addColumn(SamplePerson::getUsername).setAutoWidth(true).setHeader("Username");
        grid.addColumn(SamplePerson::isTravelling).setAutoWidth(true).setHeader("Is Travelling?");


        grid.setDataProvider(new CrudServiceDataProvider <>(samplePersonService));//TODO izmeni - TEST SLUCAJ
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.setHeightFull();

        filterText.setPrefixComponent(VaadinIcon.SEARCH.create());
        filterText.setPlaceholder("...");
        filterText.setClearButtonVisible(true);

        addClassNames("find-dabblers-view", "flex", "flex-col", "h-full");
        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(SAMPLEPERSON_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(FindDabblersView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(SamplePerson.class);

        // Bind fields. This is where you'd define e.g. validation rules

        filterText.setValueChangeMode(ValueChangeMode.EAGER);
        filterText.addValueChangeListener(event -> this.onFilterChange(grid));
        filterText.setSizeFull();

        HeaderRow filterRow = grid.appendHeaderRow();

        filterRow.getCell(grid.getColumns().get(0)).setComponent(filterText);

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            //TODO Add User as Friend
        });

    }

    private void onFilterChange(Grid<SamplePerson> grid) {
            ListDataProvider<SamplePerson> listDataProvider = (ListDataProvider<SamplePerson>) grid.getDataProvider();
            listDataProvider.setFilter(item -> {
                boolean filterMatch = true;

                if(!filterText.isEmpty()){
                    filterMatch = item.toString().toLowerCase(Locale.ROOT)
                            .contains(filterText.getValue().toLowerCase(Locale.ROOT));
                }

                return filterMatch;
            });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Integer> samplePersonId = event.getRouteParameters().getInteger(SAMPLEPERSON_ID);
        if (samplePersonId.isPresent()) {
            Optional<SamplePerson> samplePersonFromBackend = samplePersonService.get(samplePersonId.get());
            if (samplePersonFromBackend.isPresent()) {
                populateForm(samplePersonFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested samplePerson was not found, ID = %d", samplePersonId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(FindDabblersView.class);
            }
        }
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("flex flex-col");
        editorLayoutDiv.setWidth("400px");

        Div editorDiv = new Div();
        editorDiv.setClassName("p-l flex-grow");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        firstName = new TextField("First Name");
        firstName.setReadOnly(true);
        lastName = new TextField("Last Name");
        lastName.setReadOnly(true);
        email = new TextField("Email");
        email.setReadOnly(true);
        phone = new TextField("Phone");
        phone.setReadOnly(true);
        dateOfBirth = new DatePicker("Date Of Birth");
        dateOfBirth.setReadOnly(true);
        occupation = new TextField("Occupation");
        occupation.setReadOnly(true);

        Component[] fields = new Component[]{firstName, lastName, email, phone, dateOfBirth, occupation};

        for (Component field : fields) {
            ((HasStyle) field).addClassName("full-width");
        }
        formLayout.add(fields);
        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("w-full flex-wrap bg-contrast-5 py-s px-l");
        buttonLayout.setSpacing(true);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        buttonLayout.add(save, cancel);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setId("grid-wrapper");
        wrapper.setWidthFull();
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(SamplePerson value) {
        this.samplePerson = value;
        binder.readBean(this.samplePerson);

    }
}
