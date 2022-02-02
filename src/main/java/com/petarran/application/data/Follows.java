package com.petarran.application.data;

import org.springframework.data.neo4j.core.schema.*;

@RelationshipProperties
public class Follows {
    @Id
    @GeneratedValue
    private Long id;

    @TargetNode
    private User userFollowed;

    @Property
    private String followedId;

    public Follows(User userFollowed, String followedId) {
        this.id = null;
        this.userFollowed = userFollowed;
        this.followedId = followedId;
    }

    public Follows withId(Long id) {
        if (this.id.equals(id)) {
            return this;
        } else {
            return new Follows(this.userFollowed, this.followedId);
        }
    }

    public String getFollowedId() {
        return followedId;
    }

    public void setFollowedId(String followedId) {
        this.followedId = followedId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUserFollowed() {
        return userFollowed;
    }

    public void setUserFollowed(User userFollowed) {
        this.userFollowed = userFollowed;
    }
}
