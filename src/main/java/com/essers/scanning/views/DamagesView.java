package com.essers.scanning.views;

import com.essers.scanning.data.model.DamageReport;
import com.essers.scanning.data.model.Movement;
import com.essers.scanning.data.service.DamageReportService;
import com.essers.scanning.data.service.MovementService;
import com.essers.scanning.views.components.DamageItem;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;
import java.util.List;

@PermitAll
@Route(value = "damages", layout = MainLayout.class)
final class DamagesView extends VerticalLayout {
    private List<DamageReport> damageReports;

    public DamagesView(MovementService movementService, DamageReportService damageReportService) {
        //check if user jas active movement, if not redirect to portal
        Movement movement = movementService.getAssignedMovement();
        if (movement == null) {
            UI.getCurrent().getPage().setLocation("/portal");
            return;
        }

        //create back button
        Icon icon = new Icon(VaadinIcon.ARROW_LONG_LEFT);
        icon.addClickListener(e -> UI.getCurrent().getPage().setLocation("/scanning"));
        add(icon);

        //get damage reports
        damageReports = damageReportService.getAllDamagesOfMovement(movement);

        //if damage reports are empty, show message
        if (damageReports.isEmpty()) {
            Image image = new Image("images/empty.svg", "Empty");
            image.setWidthFull();
            add(new H2("No damages reported yet"), image);
        } else {
            //show damage reports
            H2 damagesTitle = new H2("DAMAGES (" + damageReports.size() + ")");
            damagesTitle.getStyle().set("margin", "0");
            H3 productTitle = new H3(movement.getProduct().getName());
            productTitle.getStyle().set("margin", "0");
            add(damagesTitle, productTitle);
            damageReports.forEach(report -> add(new DamageItem(report)));
        }
    }

}
