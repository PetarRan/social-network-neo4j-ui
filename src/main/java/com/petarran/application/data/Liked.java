package com.petarran.application.data;

import org.springframework.data.neo4j.core.schema.*;

@RelationshipProperties
public class Liked {
    @Id
    @GeneratedValue
    private Long id;
    @TargetNode
    private Post post;

    public Liked(Post post) {
        this.id = null;
        this.post = post;
    }

    public Liked withId(Long id) {
        if (this.id.equals(id)) {
            return this;
        } else {
            return new Liked(this.post);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
