package com.petarran.application.data;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@RelationshipProperties
public class Follows {
    @Id
    @GeneratedValue
    private Long id;

    @TargetNode
    private User userFollowed;

    public Follows(User userFollowed) {
        this.id = null;
        this.userFollowed = userFollowed;
    }

    public Follows withId(Long id) {
        if (this.id.equals(id)) {
            return this;
        } else {
            return new Follows(this.userFollowed);
        }
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
