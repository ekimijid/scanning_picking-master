package com.essers.scanning.views;

import com.essers.scanning.data.model.Movement;
import com.essers.scanning.data.service.MovementService;
import com.essers.scanning.views.components.Card;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;

import static com.vaadin.flow.component.button.ButtonVariant.LUMO_PRIMARY;
import static com.vaadin.flow.component.icon.VaadinIcon.ARROW_LEFT;
import static com.vaadin.flow.component.icon.VaadinIcon.ARROW_RIGHT;
import static com.vaadin.flow.component.notification.Notification.Position.TOP_END;
import static com.vaadin.flow.component.notification.NotificationVariant.LUMO_ERROR;
import static com.vaadin.flow.component.notification.NotificationVariant.LUMO_SUCCESS;

@PermitAll
@Route(value = "scanning", layout = MainLayout.class)
final class ScanningView extends VerticalLayout implements BeforeEnterObserver {

    //constants
    private static final String SCAN_FORM_CLASS = "scan-form";
    private static final int ERROR_NOTIFICATION_DURATION_IN_MILLIS = 4000;
    private static final String PRIMARY_BUTTON_CLASS = "primary-button";
    private static final String CONTROL_ICON_CLASS = "control-icon";
    private static final String CONTROL_TEXT_CLASS = "control-text";
    private static final String SCANNING_TITLE_CLASS = "scanning-title";
    private static final String ML_AUTO_CLASS = "ml-auto";
    private static final int FIRST_STEP = 1;

    //services and variables
    private final MovementService movementService;
    private final Movement movement;
    private final TextField barcode = new TextField("Barcode");
    private final TextField location = new TextField("Location");
    private final Button submit = new Button("Submit");
    private final HorizontalLayout next = new HorizontalLayout();
    private final HorizontalLayout back = new HorizontalLayout();
    private final Notification notification = new Notification();

    //used in FPP barcode/location form switching
    private int currentStep = 1;


    public ScanningView(MovementService movementService) {
        String javascript = """
                $(document).ready(() => {$(document).keydown(e => {
                if ( $('input:focus').length > 0 ) {  return; }
                if(e.key.length !== 1){return;}
                this.$server.handleKeyPress(e.key)
                })});
                """;

        //simple javascript to handle keypresses
        getElement().executeJs(javascript);

        //init movement service
        this.movementService = movementService;

        //get assigned movement
        movement = movementService.getAssignedMovement();
        configureNotification();

        //if no movement is assigned, show waiting illustration

        if (movement == null) {
            setJustifyContentMode(JustifyContentMode.CENTER);
            setAlignItems(Alignment.CENTER);

            //title
            H3 scanningTitle = new H3("No tasks available...");
            scanningTitle.addClassName(SCANNING_TITLE_CLASS);

            //illustration
            Image image = new Image("images/waiting.svg", "Scanning");
            image.setWidthFull();

            //back button
            Button backButton = new Button("Back to portal", e -> UI.getCurrent().getPage().setLocation("portal"));

            add(scanningTitle, image, backButton);

        } else {
            //if movement is assigned, show barcode/location form
            //set location off screen by 150px (used later for animation)
            location.getElement().getStyle().set("transform", "translateX(150%)");

            //title
            H3 scanningTitle = new H3("Waiting for scan...");
            scanningTitle.addClassName(SCANNING_TITLE_CLASS);

            add(scanningTitle, new Card(movement), getScanForm(), getControls(), getNavigationButtons());

        }


    }

    private static HorizontalLayout getNavigationButtons() {
        //report damage button
        Button reportDamage = new Button("Report damage");
        reportDamage.addClassNames(PRIMARY_BUTTON_CLASS);
        reportDamage.addClickListener(e -> UI.getCurrent().getPage().setLocation("reportdamage"));

        //view damages button
        Button viewDamages = new Button("View damages");
        viewDamages.addClickListener(e -> UI.getCurrent().getPage().setLocation("damages"));
        reportDamage.addClassNames(PRIMARY_BUTTON_CLASS);

        //put navigation buttons in horizontal layout
        return new HorizontalLayout(reportDamage, viewDamages);

    }

    private static void configureControlButton(HorizontalLayout button, Icon icon, String text) {
        icon.addClassName(CONTROL_ICON_CLASS);
        Span controlText = new Span(text);
        controlText.addClassName(CONTROL_TEXT_CLASS);
        button.add(icon, controlText);
        button.setAlignItems(Alignment.CENTER);
        button.getStyle().set("gap", "0 !important");
    }

    private void configureNotification() {
        notification.setText("Please enter a barcode and location");
        notification.setDuration(ERROR_NOTIFICATION_DURATION_IN_MILLIS);
        notification.setPosition(TOP_END);
        notification.addThemeVariants(LUMO_ERROR);
    }

    private HorizontalLayout getControls() {
        //add click listeners to buttons
        back.addClickListener(e -> previousStep());
        next.addClickListener(e -> nextStep());

        //configure buttons
        configureControlButton(back, new Icon(ARROW_LEFT), "Back");
        configureControlButton(next, new Icon(ARROW_RIGHT), "Next");

        //individual button configuration
        back.setVisible(false);
        next.addClassName(ML_AUTO_CLASS);

        //configure submit button
        submit.addThemeVariants(LUMO_PRIMARY);
        submit.addClassName(ML_AUTO_CLASS);
        submit.setVisible(false);
        submit.addClickListener(e -> handleSubmit());

        //add buttons to layout
        HorizontalLayout controls = new HorizontalLayout();
        controls.setSizeFull();
        controls.add(back, next, submit);
        return controls;
    }

    private void handleSubmit() {
        //if barcode or location is empty, show notification
        if (barcode.getValue() == null || barcode.getValue().isEmpty()
                || location.getValue() == null || location.getValue().isEmpty()) {
            notification.open();
        } else if (!movement.getLocationTo().equals(location.getValue())) {
            //if location does not match movement location, show notification
            notification.setText("Location does not match");
            notification.open();
        } else {
            //handle submit if everything is correct
            movementService.handleSubmit(movement);
        }


    }


    @ClientCallable
    private void handleKeyPress(String key) {
        //if step is 1, handle keypress for barcode
        if (currentStep == FIRST_STEP) {
            barcode.setValue(barcode.getValue() + key);
        } else {
            //if step is 2, handle keypress for location
            location.setValue(location.getValue() + key);
        }
    }

    private void nextStep() {
        //if barcode does not match movement barcode, show notification
        if (!movement.getProduct().getBarcode().equals(barcode.getValue())) {
            notification.setText("Unexpected barcode scanned");
            notification.open();
            return;
        }

        //increase step and update UI
        currentStep++;
        back.setVisible(true);
        next.setVisible(false);
        submit.setVisible(true);

        //apply animations
        barcode.addClassName("slide-out-animation");
        location.addClassName("slide-in-animation");
        barcode.removeClassName("slide-in-animation-2");
        location.removeClassName("slide-out-animation-2");
    }

    private void previousStep() {

        //decrease step and update UI if step is greater than 1
        currentStep--;
        if (currentStep == 1) {
            back.setVisible(false);
            submit.setVisible(false);
            next.setVisible(true);
        }

        //apply animations
        barcode.removeClassName("slide-out-animation");
        location.removeClassName("slide-in-animation");
        barcode.addClassName("slide-in-animation-2");
        location.addClassName("slide-out-animation-2");
    }

    private Div getScanForm() {
        Div form = new Div();
        form.setWidthFull();
        form.addClassName(SCAN_FORM_CLASS);
        barcode.setWidthFull();
        barcode.setPlaceholder("Scan barcode");
        location.setWidthFull();
        location.setPlaceholder("Scan location");

        form.add(barcode, location);
        return form;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        Notification errorNotification = new Notification("Damage successfully reported");
        errorNotification.setDuration(ERROR_NOTIFICATION_DURATION_IN_MILLIS);
        errorNotification.setPosition(TOP_END);
        errorNotification.addThemeVariants(LUMO_SUCCESS);
        //If error parameter is set show notification
        if (beforeEnterEvent.getLocation().getQueryParameters().getParameters().containsKey("damagereported")) {
            errorNotification.open();
        }
    }
}
