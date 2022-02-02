package com.petarran.application.feign_client;

import com.petarran.application.data.User;
import feign.Param;
import feign.RequestLine;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Collection;

public interface UserFeignClient extends CommonFeignClient<User> {

    @RequestLine("GET getAll")
    Collection<User> findAllUsers();

    @RequestLine("GET getByMail/{mail}")
    User findUserByMail(@Param("mail")String mail);

    @RequestLine("POST addUser")
    void addUser(@Valid @RequestBody(required = true) User user);

    @RequestLine("DELETE deleteProfile")
    void removeUser(@Valid @RequestBody(required = true) User user);

    @RequestLine("PUT updateUser")
    void updateUser(@Valid @RequestBody(required = true) User user);

    @RequestLine("GET allMyFollowers/{mail}")
    Collection<User> findAllMyFollowers(@Param("mail")String mail);

}
