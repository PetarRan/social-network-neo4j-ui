package com.petarran.application.views;

import com.petarran.application.feign_client.PostFeignClient;
import com.petarran.application.feign_client.UserFeignClient;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

@PageTitle("Login")
@Route(value = "Login", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class LoginView extends Div {

    public LoginView(UserFeignClient userFeignClient, PostFeignClient postFeignClient) {

        addClassName("wall2-view");
        setSizeFull();
    }

}
