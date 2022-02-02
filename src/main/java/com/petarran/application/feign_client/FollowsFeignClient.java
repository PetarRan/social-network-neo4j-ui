package com.petarran.application.feign_client;

import com.petarran.application.data.Post;
import com.petarran.application.data.User;
import feign.Param;
import feign.RequestLine;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.Collection;

public interface FollowsFeignClient extends CommonFeignClient<Post> {

    @RequestLine("POST followUser?mail={mail}")
    void followUser(@Valid @RequestBody(required = true) User user, @Param("mail")String userEmail);

    @RequestLine("DELETE unfollowUser?mail={mail}")
    void unfollowUser(@Valid @RequestBody(required = true)User user, @Param("mail")String userEmail);

    @RequestLine("GET getFollowing/{mail1}/{mail2}")
    boolean amIFollowing(@Param("mail1")String mail, @Param("mail2")String myMail);



}
