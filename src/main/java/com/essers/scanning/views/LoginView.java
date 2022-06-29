package com.essers.scanning.views;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;

import static com.vaadin.flow.component.notification.Notification.Position.TOP_END;
import static com.vaadin.flow.component.notification.NotificationVariant.LUMO_ERROR;

//Link loginview stylesheet to certain components
@Route("login")
public final class LoginView extends VerticalLayout implements BeforeEnterObserver {
    public static final String MARGIN_TOP_5_CLASS = "mt-5";
    public static final String LOGIN_LOGO_PATH = "";
    public static final String LOGIN_ACTION_ROUTE = "/login";
    public static final int ERROR_NOTIFICATION_DURATION_IN_MILLIS = 4000;

    public LoginView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);

        VerticalLayout loginLayout = new VerticalLayout();
        //margin top 5rem
        loginLayout.addClassName(MARGIN_TOP_5_CLASS);
        loginLayout.add(getLoginLogo(), getLoginForm());

        add(loginLayout);
    }


    //Add Login form
    private static LoginForm getLoginForm() {
        LoginForm loginForm = new LoginForm();

        loginForm.setForgotPasswordButtonVisible(false);
        //Set loginform action to /login (post)
        loginForm.setAction(LOGIN_ACTION_ROUTE);
        return loginForm;
    }

    private static Image getLoginLogo() {
        Image loginLogo = new Image("images/HEssers_Logo.svg", "Logo essers");
        loginLogo.setWidthFull();
        return loginLogo;
    }

    //Check for error parameter before enter
    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        //create Error notification with 4 second duration
        Notification errorNotification = new Notification("Username and password combination does not match");
        errorNotification.setDuration(ERROR_NOTIFICATION_DURATION_IN_MILLIS);
        errorNotification.setPosition(TOP_END);
        errorNotification.addThemeVariants(LUMO_ERROR);
        //If error parameter is set show notification
        if (beforeEnterEvent.getLocation().getQueryParameters().getParameters().containsKey("error")) {
            errorNotification.open();
        }
    }
}
