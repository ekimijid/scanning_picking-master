package com.essers.scanning.views;


import com.essers.scanning.data.model.Movement;
import com.essers.scanning.data.service.MovementService;
import com.essers.scanning.security.SecurityService;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;

/**
 * The main view is a top-level placeholder for other views.
 */
@CssImport(value = "./styles/loginViewStyles.css")
@CssImport(value = "./styles/loginViewStyles.css",
        themeFor = "vaadin-login-form vaadin-notification-card " +
                "vaadin-login-form-wrapper vaadin-notification-card " +
                "vaadin-dialog-overlay")

//import jquery cdn
@JavaScript("https://cdnjs.cloudflare.com/ajax/libs/jquery/3.4.1/jquery.min.js")
final class MainLayout extends AppLayout {
    private final transient MovementService movementService;
    private final Dialog dialog = new Dialog();

    public MainLayout(MovementService movementService) {
        //assign movement service & security service
        this.movementService = movementService;


        //assign dialog action
        dialog.setCloseOnEsc(true);
        //create confirm button that abandons the current movement and logs the user out
        Button confirm = new Button("Abandon");
        confirm.addClickListener(e -> {
            movementService.abandonMovement();
            SecurityService.logout();
        });

        //setup dialog layout
        dialog.add(new H3("Abandon current movement?"),
                new Paragraph(("Are you sure you want to abandon the current movement?")),
                confirm);

        //create drawer toggle button
        DrawerToggle toggle = new DrawerToggle();
        H1 title = new H1("Scanner PWS");
        title.getStyle()
                .set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");
        Tabs tabs = getTabs();

        addToDrawer(tabs);
        addToNavbar(toggle, title);
    }

    private Tabs getTabs() {
        Span logoutSpan = new Span("Log out");
        logoutSpan.addClickListener(e -> {
            Movement movement = movementService.getAssignedMovement();
            if (movement != null) {
                dialog.open();
            } else {
                SecurityService.logout();
            }
        });
        Icon icon = VaadinIcon.STORAGE.create();
        icon.getStyle()
                .set("box-sizing", "border-box")
                .set("margin-inline-end", "var(--lumo-space-m)")
                .set("margin-inline-start", "var(--lumo-space-xs)")
                .set("padding", "var(--lumo-space-xs)");

        RouterLink link = new RouterLink();
        link.getStyle().set("background-color", "#FBFCFC");

        link.add(icon, new Span("Portal"));
        // Demo has no routes

        link.setRoute(PortalView.class);
        link.setTabIndex(-1);
        Tabs tabs = new Tabs();
        tabs.add(
                new Tab(link),
                new Tab(logoutSpan)
        );
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        return tabs;
    }

}
