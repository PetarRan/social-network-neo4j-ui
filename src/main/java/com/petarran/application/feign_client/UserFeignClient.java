package com.petarran.application.feign_client;

import com.petarran.application.data.User;
import feign.Param;
import feign.RequestLine;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.Collection;

public interface UserFeignClient extends CommonFeignClient<User> {

    @RequestLine("GET getAll")
    Collection<User> findAllUsers();

    @RequestLine("GET getByMail/{mail}")
    Collection<User> findUserByMail();

    @RequestLine("POST addUser")
    void addUser(@Valid @RequestBody(required = true) User user);

    @RequestLine("PUT updateUser")
    void updateUser(@Valid @RequestBody(required = true) User user);

}
