package com.essers.scanning.views.components;

import com.essers.scanning.data.model.Movement;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public final class Card extends Div {
    private static final String CARD_CLASS_NAME = "card";
    private Movement movement;

    public Card(Movement movement) {
        this.movement = movement;

        addClassName(CARD_CLASS_NAME);
        add(getHeader(), getBody());
    }

    private static HorizontalLayout getInfo(Icon icon, String title, String content) {
        icon.setColor("#77828F");
        icon.addClassName("card-icon");
        HorizontalLayout infoContainer = new HorizontalLayout();
        infoContainer.addClassName("info-container");
        Span titleSpan = new Span(title);
        Span contentSpan = new Span(content);
        titleSpan.addClassName("card-content-title");
        contentSpan.addClassName("card-content");
        HorizontalLayout text = new HorizontalLayout();
        text.addClassName("card-info-text");
        text.add(titleSpan, contentSpan);
        infoContainer.add(icon, text);
        infoContainer.setAlignItems(FlexComponent.Alignment.CENTER);
        return infoContainer;
    }

    private Div getHeader() {
        Div header = new Div();
        header.addClassName("card-header");
        H3 title = new H3(movement.getProduct().getName());
        title.addClassName("card-title");
        header.add(title);
        Paragraph description = new Paragraph(movement.getProduct().getDescription());
        description.addClassName("card-description");
        header.add(description);
        return header;
    }

    private VerticalLayout getBody() {
        VerticalLayout body = new VerticalLayout();
        body.addClassName("card-body");
        body.add(getInfo(new Icon(VaadinIcon.PACKAGE), "QTY: ", movement.getQuantity() + " " + movement.getUom()));

        body.add(getInfo(new Icon(VaadinIcon.MAP_MARKER), "FROM: ", movement.getLocationFrom()));


        body.add(getInfo(new Icon(VaadinIcon.MAP_MARKER), "TO: ", movement.getLocationTo()));
        body.setSizeFull();

        return body;
    }

    public Movement getMovement() {
        return movement;
    }

    public void setMovement(Movement movement) {
        this.movement = movement;
    }
}
