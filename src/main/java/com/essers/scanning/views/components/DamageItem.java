package com.essers.scanning.views.components;

import com.essers.scanning.data.model.DamageReport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamResource;

import java.io.ByteArrayInputStream;
import java.util.Base64;

import static com.vaadin.flow.component.icon.VaadinIcon.CHECK;
import static com.vaadin.flow.component.icon.VaadinIcon.CLOSE;
import static com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode.BETWEEN;

public final class DamageItem extends VerticalLayout {
    //constant classes
    private static final String DAMAGE_ITEM_CLASS = "damage-item";
    private static final String DAMAGE_ITEM_TITLE_CLASS = "damage-item-title";
    private static final String DAMAGE_ITEM_DESCRIPTION_CLASS = "damage-item-description";
    private static final String DAMAGE_ITEM_ICON_CONTAINER_CLASS
            = "damage-item-icon-container";
    private static final String DAMAGE_ITEM_INFO_TEXT_CLASS = "damage-item-info-text";
    private static final String DAMAGE_ITEM_ICON_CLASS = "damage-item-icon";
    private static final String NOT_SHIPPABLE_ICON_CLASS = "not-shippable-icon";
    private static final String SHIPPABLE_ICON_CLASS = "shippable-icon";
    //constant variables
    private static final String DIALOG_IMAGE_MAX_WIDTH = "100%";
    private static final String DAMAGE_ITEM_WIDTH = "90%";
    private final Dialog imageDialog = new Dialog();
    private final DamageReport damageReport;

    public DamageItem(DamageReport damageReport) {
        this.damageReport = damageReport;
        addClassName(DAMAGE_ITEM_CLASS);
        setWidth(DAMAGE_ITEM_WIDTH);

        //title
        H5 damageItemTitle = new H5("Description");
        damageItemTitle.addClassName(DAMAGE_ITEM_TITLE_CLASS);


        add(damageItemTitle, getDamageItemImageContainer(), getDamageItemDetails());


    }

    private HorizontalLayout getDamageItemImageContainer() {
        //image description
        Paragraph damageItemDescription = new Paragraph(damageReport.getDescription());
        damageItemDescription.addClassName(DAMAGE_ITEM_DESCRIPTION_CLASS);

        //image
        HorizontalLayout damageItemImageContainer = new HorizontalLayout();
        damageItemImageContainer.setWidthFull();
        damageItemImageContainer.setJustifyContentMode(BETWEEN);
        StreamResource streamResource = new StreamResource("img.png",
                () -> new ByteArrayInputStream(Base64.getDecoder().decode(damageReport.getImage())));
        Image damageItemImage = new Image(streamResource, "Image");
        damageItemImage.setWidth("120px");
        damageItemImage.setMaxWidth("100%");

        //image dialog
        Image dialogImage = new Image(streamResource, "Image");
        dialogImage.setWidthFull();
        dialogImage.setMaxWidth(DIALOG_IMAGE_MAX_WIDTH);
        imageDialog.add(dialogImage);
        damageItemImage.addClickListener(e -> imageDialog.open());

        damageItemImageContainer.add(damageItemDescription, damageItemImage);
        return damageItemImageContainer;
    }

    private HorizontalLayout getDamageItemDetails() {
        //details
        HorizontalLayout damageItemDetails = new HorizontalLayout();
        damageItemDetails.setJustifyContentMode(BETWEEN);

        //left side of details
        Div damageItemDetailsLeft = new Div();
        damageItemDetailsLeft.addClassName(DAMAGE_ITEM_ICON_CONTAINER_CLASS);
        Paragraph damageReportedBy = new Paragraph("Reported by: " + damageReport.getUser().getUsername());
        damageReportedBy.addClassName(DAMAGE_ITEM_INFO_TEXT_CLASS);
        damageItemDetailsLeft.add(damageReportedBy);

        //right side of details
        Div damageItemDetailsRight = new Div(getRightSideOfDetails());
        damageItemDetailsRight.addClassName(DAMAGE_ITEM_ICON_CONTAINER_CLASS);

        damageItemDetails.add(damageItemDetailsLeft, damageItemDetailsRight);
        return damageItemDetails;
    }

    private Paragraph getRightSideOfDetails() {
        Paragraph fitForShipping = new Paragraph();
        fitForShipping.addClassName(DAMAGE_ITEM_INFO_TEXT_CLASS);

        //shippable check
        if (damageReport.isShippable()) {
            //text
            fitForShipping.add("Fit for shipping");

            //icon
            Icon check = new Icon(CHECK);
            check.addClassNames(SHIPPABLE_ICON_CLASS, DAMAGE_ITEM_ICON_CLASS);
            fitForShipping.add(check);
        } else {
            //text
            fitForShipping.add("Not fit for shipping");

            //icon
            Icon cross = new Icon(CLOSE);
            cross.addClassNames(NOT_SHIPPABLE_ICON_CLASS, DAMAGE_ITEM_ICON_CLASS);
            fitForShipping.add(cross);
        }
        return fitForShipping;
    }
}
