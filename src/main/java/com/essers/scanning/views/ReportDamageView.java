package com.essers.scanning.views;

import com.essers.scanning.data.model.DamageReport;
import com.essers.scanning.data.model.Movement;
import com.essers.scanning.data.service.DamageReportService;
import com.essers.scanning.data.service.MovementService;
import com.essers.scanning.security.SecurityService;
import com.essers.scanning.views.components.Card;
import com.essers.scanning.views.utils.FileInputException;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.History;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.Route;
import org.apache.commons.io.IOUtils;

import javax.annotation.security.PermitAll;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.logging.Logger;

import static com.vaadin.flow.component.notification.Notification.Position.TOP_END;
import static com.vaadin.flow.component.notification.NotificationVariant.LUMO_ERROR;

@PermitAll
@Route(value = "reportdamage", layout = MainLayout.class)
final class ReportDamageView extends VerticalLayout {

    //constants
    private static final int ERROR_NOTIFICATION_DURATION_IN_MILLIS = 4000;
    private static final String DESCRIPTION_HEIGHT = "100px";
    private static final Logger LOGGER = Logger.getLogger(ReportDamageView.class.getName());
    //services, models and variables
    private final transient DamageReportService damageReportService;
    private final transient SecurityService securityService;
    private final Movement movement;
    private final Notification notification = new Notification();
    private final TextArea description = new TextArea();
    private final Checkbox fitForShipping = new Checkbox("Fit for shipping");
    private final MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
    private byte[] imageBytes;


    public ReportDamageView(MovementService movementService,
                            DamageReportService damageReportService,
                            SecurityService securityService) {
        //initialize logger
        //initialize services
        this.damageReportService = damageReportService;
        this.securityService = securityService;

        History history = UI.getCurrent().getPage().getHistory();

        setSizeFull();
        movement = movementService.getAssignedMovement();
        //if no movement is assigned, show waiting illustration
        if (movement == null) {
            UI.getCurrent().getPage().setLocation("portal");
        } else {

            H1 reportDamageTitle = new H1("Report Damage");
            reportDamageTitle.addClassName("report-damage-title");
            Icon icon = new Icon(VaadinIcon.ARROW_LONG_LEFT);
            icon.addClickListener(e -> history.back());
            getStyle().set("padding", "0 16px");
            getStyle().set("gap", "0");
            add(icon, reportDamageTitle, new Card(movement), getReportDamageForm());
            configureNotification();
        }

    }

    private void configureNotification() {
        notification.setText("Please insert a description and upload an image");
        notification.setDuration(ERROR_NOTIFICATION_DURATION_IN_MILLIS);
        notification.setPosition(TOP_END);
        notification.addThemeVariants(LUMO_ERROR);
    }

    private VerticalLayout getReportDamageForm() {
        VerticalLayout reportDamageForm = new VerticalLayout();
        reportDamageForm.getStyle().set("gap", "0.2em");
        reportDamageForm.setSizeFull();


        //description
        description.setLabel("Description");
        description.setPlaceholder("Describe the damage");
        description.setWidthFull();
        description.setHeight(DESCRIPTION_HEIGHT);

        //upload form
        Upload upload = new Upload(buffer);
        upload.setWidthFull();
        upload.setAcceptedFileTypes("image/tiff", ".png", ".jpg", ".webp", ".jpeg", ".bmp");
        upload.addSucceededListener(event -> {
            try {
                handleFileUpload(event);
            } catch (FileInputException e) {
                String message = "Something went wrong with the file upload: " + e.getMessage();
                LOGGER.warning(message);
            }
        });


        upload.setMaxFiles(1);

        //submit button
        Button submit = new Button("Submit");
        submit.addClickListener(event -> handleSubmit());
        submit.getStyle().set("margin-top", "auto");

        reportDamageForm.add(description, fitForShipping, upload, submit);
        return reportDamageForm;
    }

    private void handleFileUpload(SucceededEvent event) throws FileInputException {
        try {
            String filename = event.getFileName();
            InputStream inputStream = buffer.getInputStream(filename);

            imageBytes = IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            throw new FileInputException("Something went wrong with the file upload", e);
        }
    }

    private void handleSubmit() {
//        show notification when description and image is empty
        if (description.getValue().isEmpty() || buffer.getFiles().isEmpty()) {
            notification.open();
        } else {
            DamageReport damageReport = new DamageReport(movement, securityService.getAuthenticatedUser(),
                    description.getValue(), fitForShipping.getValue(),
                    Base64.getEncoder().encodeToString(imageBytes));
            damageReportService.save(damageReport);
            UI.getCurrent().getPage().setLocation("scanning?damagereported");
        }
    }
}
