package com.midterm.diary.model;

public class Post {


    public Post(){}
    private String id;

    private String title;
    private String content;
    private String color;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String date;
    public Post(String id, String title, String content, String color, String date) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.color = color;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }



}
