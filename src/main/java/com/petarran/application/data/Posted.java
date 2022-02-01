package com.petarran.application.data;


import org.hibernate.annotations.Target;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@RelationshipProperties
public class Posted {
    @Id
    @GeneratedValue
    private Long id;
    @TargetNode
    private Post post;

    public Posted(Post post) {
        this.id = null;
        this.post = post;
    }

    public Posted withId(Long id) {
        if (this.id.equals(id)) {
            return this;
        } else {
            return new Posted(this.post);
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
