package com.vicevi.crozproblem.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Joke {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String content;
    private int likes;
    private int dislikes;

    public Joke() {}

    public Joke(String content, Category category) {
        this.content = content;
        this.category = category;
    }

    @ManyToOne
    @JoinColumn(nullable=false)
    private Category category;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategories(Category category) {
        this.category = category;
    }
}
