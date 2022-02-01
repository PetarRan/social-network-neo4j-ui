package com.petarran.application.data;

import org.neo4j.ogm.annotation.*;

@RelationshipEntity(type = "Liked")
public class Liked {
    @Id
    @GeneratedValue
    private Long id;
    @StartNode
    private User user;
    @EndNode
    private Post post;

    public Liked(User user, Post post) {
        this.id = null;
        this.user = user;
        this.post = post;
    }

    public Liked withId(Long id) {
        if (this.id.equals(id)) {
            return this;
        } else {
            return new Liked(this.user, this.post);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
