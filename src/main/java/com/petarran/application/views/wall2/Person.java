package com.petarran.application.views.wall2;

public class Person {

    private String image;
    private String name;
    private boolean travelStatus;
    private String post;
    private String likes;
    private String location;

    public Person() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String isTravelStatus() {
        if(travelStatus) return "Currently Travelling"; else return "Currently at Home";
    }

    public void setTravelStatus(Boolean travelStatus) {
        this.travelStatus = travelStatus;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String likes) {
        this.location = likes;
    }

}
