package com.vicevi.crozproblem.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class JokeForm {

    @NotBlank(message = "Please add content to joke")
    private String content;

    @NotNull(message = "Please choose a category")
    private int categoryId;

    public JokeForm() {
    }

    public JokeForm(String content, int categoryId) {
        this.content = content;
        this.categoryId = categoryId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}
