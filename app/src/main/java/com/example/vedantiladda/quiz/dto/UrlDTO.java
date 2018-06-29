package com.example.vedantiladda.quiz.dto;

public class UrlDTO {
    String url;
    String categoryId;
    String difficulty;
    String answerType;

    public String getAnswerType() {
        return answerType;
    }

    public void setAnswerType(String answerType) {
        this.answerType = answerType;
    }


    @Override
    public String toString() {
        return "UrlDTO{" +
                "url='" + url + '\'' +
                ", category='" + categoryId + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", answerType='" + answerType + '\'' +
                '}';
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

}
