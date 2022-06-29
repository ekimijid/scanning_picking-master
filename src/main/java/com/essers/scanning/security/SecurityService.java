package com.essers.scanning.security;

import com.essers.scanning.data.model.User;
import com.essers.scanning.data.service.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinServletRequest;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

@Component
public final class SecurityService {
    private static final String LOGOUT_SUCCESS_URL = "/login";
    private final UserService userService;

    public SecurityService(UserService userService) {
        this.userService = userService;
    }

    public static void logout() {
        UI.getCurrent().getPage().setLocation(LOGOUT_SUCCESS_URL);
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(
                VaadinServletRequest.getCurrent().getHttpServletRequest(), null,
                null);
    }

    public User getAuthenticatedUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context.getAuthentication() == null) {
            return null;
        }
        Object principal = context.getAuthentication().getPrincipal();
        if (principal != null) {

            return userService.findByUsername(principal.toString());
        } else {
            // Anonymous or no authentication.
            return null;
        }
    }
}
