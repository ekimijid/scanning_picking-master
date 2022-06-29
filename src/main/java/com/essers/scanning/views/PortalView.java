package com.essers.scanning.views;

import com.essers.scanning.data.model.Company;
import com.essers.scanning.data.model.MovementType;
import com.essers.scanning.data.model.User;
import com.essers.scanning.data.service.CompanyService;
import com.essers.scanning.data.service.MovementService;
import com.essers.scanning.data.service.MovementTypeService;
import com.essers.scanning.data.service.UserService;
import com.essers.scanning.security.SecurityService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import javax.annotation.security.PermitAll;

import static com.vaadin.flow.component.notification.Notification.Position.TOP_END;
import static com.vaadin.flow.component.notification.NotificationVariant.LUMO_ERROR;

@PermitAll
@Route("portal")
@RouteAlias(value = "")
final class PortalView extends VerticalLayout {
    //constants
    private static final int ERROR_NOTIFICATION_DURATION_IN_MILLIS = 4000;
    private static final String MARGIN_LEFT_AUTO_CLASS = "ml-auto";
    private static final String PRIMARY_BUTTON_CLASS = "primary-button";
    private static final String MARGIN_TOP_5_CLASS = "mt-5";

    //declare services and variables
    private final transient UserService userService;
    private final transient CompanyService companyService;
    private final transient MovementTypeService movementTypeService;
    private final transient SecurityService securityService;
    private final transient MovementService movementService;
    private final Notification notification = new Notification();
    private final ComboBox<Company> companyComboBox = new ComboBox<>("Company");
    private final ComboBox<MovementType> movementTypeComboBox = new ComboBox<>("Action");

    public PortalView(UserService userService, CompanyService companyService,
                      MovementTypeService movementTypeService, SecurityService securityService,
                      MovementService movementService) {
        //initialize services
        this.userService = userService;
        this.companyService = companyService;
        this.movementTypeService = movementTypeService;
        this.securityService = securityService;
        this.movementService = movementService;

        //styling
        setSizeFull();
        Image essersLogo = new Image("images/HEssers_Logo.svg", "Logo essers");
        essersLogo.setWidthFull();
        essersLogo.addClassNames(MARGIN_TOP_5_CLASS);
        add(essersLogo, getCompanyComboBox(), getMovementTypeComboBox(), getContinueButton());
        configureNotification();
    }

    private void configureNotification() {
        notification.setText("Please select a company and an action");
        notification.setDuration(ERROR_NOTIFICATION_DURATION_IN_MILLIS);
        notification.setPosition(TOP_END);
        notification.addThemeVariants(LUMO_ERROR);
    }

    private Button getContinueButton() {
        Button continueButton = new Button("Continue");
        continueButton.addClassNames(PRIMARY_BUTTON_CLASS, MARGIN_LEFT_AUTO_CLASS);
        continueButton.addClickListener(e -> handleContinueButtonClick());
        continueButton.addClickShortcut(Key.ENTER);
        return continueButton;
    }

    private ComboBox<MovementType> getMovementTypeComboBox() {
        movementTypeComboBox.setItems(movementTypeService.findAll());
        movementTypeComboBox.setItemLabelGenerator(MovementType::getName);
        movementTypeComboBox.setWidthFull();
        movementTypeComboBox.setRequired(true);
        return movementTypeComboBox;
    }

    private ComboBox<Company> getCompanyComboBox() {
        companyComboBox.setItemLabelGenerator(Company::getName);
        companyComboBox.setItems(companyService.findAllCompaniesWithMovements());
        companyComboBox.setWidthFull();
        companyComboBox.setRequired(true);
        return companyComboBox;
    }

    private void handleContinueButtonClick() {

        if (companyComboBox.getValue() == null || movementTypeComboBox.getValue() == null) {
            //show error notification
            notification.open();
        } else {
            //save company and movementType in user
            User user = securityService.getAuthenticatedUser();
            user.setCompany(companyComboBox.getValue());
            user.setMovementType(movementTypeComboBox.getValue());
            userService.saveUser(user);
//           Assign user to movement
            movementService.assignUserToMovement();

        }

    }

}
