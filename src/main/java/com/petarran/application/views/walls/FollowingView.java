package com.petarran.application.views.walls;

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
import com.vaadin.flow.server.VaadinServletService;

import java.util.Arrays;
import java.util.List;

@PageTitle("Following")
@Route(value = "following", layout = MainLayout.class)
public class FollowingView extends Div {

    private final UserFeignClient userFeignClient;
    private final PostFeignClient postFeignClient;

    public FollowingView( UserFeignClient userFeignClient, PostFeignClient postFeignClient) {
        this.userFeignClient = userFeignClient;
        this.postFeignClient = postFeignClient;

        addClassName("wall2-view");
        setSizeFull();

        postFeignClient.findFollowersPosts(VaadinServletService.getCurrentServletRequest().getSession().getAttribute("email")
                .toString()).forEach(post -> {
            add(createCard(post));
        });

    }

    private HorizontalLayout createCard(Post wallPost) {
        User personWhoPosted = userFeignClient.findUserByMail(wallPost.getUserid());

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
        if(personWhoPosted.getTravelling()!=null){
            if(personWhoPosted.getTravelling()){
                travelStatus.setText("Travelling now.");
            } else {
                travelStatus.setText("Not Travelling.");
            }
        } else {
            travelStatus.setText("Travelling now.");
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
        map.setView(latitude, 	longitude, 6);
        map.setHeight("750px");
        map.setWidth("750px");

        dialog.setWidth("900px");
        dialog.setHeight("900px");
        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(true);

        verticalLayout.add(map);
        dialog.add(verticalLayout);
        dialog.open();
    }

}
