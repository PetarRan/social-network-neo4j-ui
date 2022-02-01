package com.petarran.application.data;

import org.neo4j.ogm.annotation.*;

@RelationshipEntity(type = "Follows")
public class Follows {
    @Id
    @GeneratedValue
    private Long id;
    @StartNode
    private User userFollowing;
    @EndNode
    private User userFollowed;

    public Follows(User userFollowing, User userFollowed) {
        this.id = null;
        this.userFollowing = userFollowing;
        this.userFollowed = userFollowed;
    }

    public Follows withId(Long id) {
        if (this.id.equals(id)) {
            return this;
        } else {
            return new Follows(this.userFollowing, this.userFollowed);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUserFollowing() {
        return userFollowing;
    }

    public void setUserFollowing(User userFollowing) {
        this.userFollowing = userFollowing;
    }

    public User getUserFollowed() {
        return userFollowed;
    }

    public void setUserFollowed(User userFollowed) {
        this.userFollowed = userFollowed;
    }
}
