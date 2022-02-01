package com.petarran.application.views.following;

import com.petarran.application.components.leafletmap.LeafletMap;
import com.petarran.application.data.Post;
import com.petarran.application.data.User;
import com.petarran.application.feign_client.PostFeignClient;
import com.petarran.application.feign_client.UserFeignClient;
import com.petarran.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.Arrays;
import java.util.List;

@PageTitle("Following")
@Route(value = "following", layout = MainLayout.class)
public class FollowingView extends Div implements AfterNavigationObserver {

    Grid<Post> grid = new Grid<>();
    private final UserFeignClient userFeignClient;
    private final PostFeignClient postFeignClient;

    public FollowingView( UserFeignClient userFeignClient, PostFeignClient postFeignClient) {
        this.userFeignClient = userFeignClient;
        this.postFeignClient = postFeignClient;

        addClassName("wall2-view");
        setSizeFull();
        grid.setHeight("100%");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
        grid.addComponentColumn(post -> createCard(post));
        add(grid);
    }

    private HorizontalLayout createCard(Post wallPost) {
        User personWhoPosted = userFeignClient.findOne(wallPost.getUserid()).getContent();

        HorizontalLayout card = new HorizontalLayout();
        card.addClassName("card");
        card.setSpacing(false);
        card.getThemeList().add("spacing-s");

        Image image = new Image();
        image.setSrc(personWhoPosted.getImageUrl());
        image.setAlt("user image");
        VerticalLayout description = new VerticalLayout();
        description.addClassName("description");
        description.setSpacing(false);
        description.setPadding(false);

        HorizontalLayout header = new HorizontalLayout();
        header.addClassName("header");
        header.setSpacing(false);
        header.getThemeList().add("spacing-s");

        Span name = new Span(personWhoPosted.getEmail());
        name.addClassName("name");
        Span travelStatus = new Span();
        if(personWhoPosted.getTravelling()){
            travelStatus.setText("Travelling now.");
        } else {
            travelStatus.setText("Not Travelling.");
        }
        travelStatus.addClassName("date");
        header.add(name, travelStatus);

        Span post = new Span(wallPost.getDescription());
        post.addClassName("post");

        HorizontalLayout actions = new HorizontalLayout();
        actions.addClassName("actions");
        actions.setSpacing(false);
        actions.getThemeList().add("spacing-s");

        Button likeIcon = new Button(VaadinIcon.HEART.create());
        likeIcon.addThemeVariants(ButtonVariant.LUMO_ERROR);
        Span likes = new Span(wallPost.getLikes().toString());
        likes.addClassName("likes");

        Button locationCheck = new Button(wallPost.getLocation() ,VaadinIcon.LOCATION_ARROW_CIRCLE.create());
        locationCheck.addClickListener(click -> {
            locationPopUp(wallPost.getLatitude(), wallPost.getLongitude());
        });

        actions.add(likeIcon, likes, locationCheck);

        description.add(header, post, actions);
        card.add(image, description);
        return card;
    }

    private void locationPopUp(Double latitude, Double longitude) {
        Dialog dialog = new Dialog();
        VerticalLayout verticalLayout = new VerticalLayout();
        LeafletMap map = new LeafletMap();
        map.setView(latitude, 	longitude, 11);
        map.setWidth("700px");
        map.setHeight("700px");

        dialog.setWidth("800px");
        dialog.setHeight("800px");
        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(true);

        verticalLayout.add(map);
        dialog.add(verticalLayout);
        dialog.open();
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        grid.setItems(postFeignClient.findAllPosts());
    }

}
