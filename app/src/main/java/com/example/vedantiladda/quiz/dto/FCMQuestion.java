package com.example.vedantiladda.quiz.dto;

import java.io.Serializable;

public class FCMQuestion implements Serializable {

    private String questionId;
    private String contestQuestionId;
    private String categoryId;
    private String difficulty;
    private String questionType;
    private String questionText;
    private String questionUrl;
    private String optionOne;
    private String optionTwo;
    private String optionThree;
    private String optionFour;
    private String answerType;
    private String points ;
    private String status;

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getContestQuestionId() {
        return contestQuestionId;
    }

    public void setContestQuestionId(String contestQuestionId) {
        this.contestQuestionId = contestQuestionId;
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

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getQuestionUrl() {
        return questionUrl;
    }

    public void setQuestionUrl(String questionUrl) {
        this.questionUrl = questionUrl;
    }

    public String getOptionOne() {
        return optionOne;
    }

    public void setOptionOne(String optionOne) {
        this.optionOne = optionOne;
    }

    public String getOptionTwo() {
        return optionTwo;
    }

    public void setOptionTwo(String optionTwo) {
        this.optionTwo = optionTwo;
    }

    public String getOptionThree() {
        return optionThree;
    }

    public void setOptionThree(String optionThree) {
        this.optionThree = optionThree;
    }

    public String getOptionFour() {
        return optionFour;
    }

    public void setOptionFour(String optionFour) {
        this.optionFour = optionFour;
    }

    public String getAnswerType() {
        return answerType;
    }

    public void setAnswerType(String answerType) {
        this.answerType = answerType;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    @Override
    public String toString() {
        return "FCMQuestion{" +
                "questionId='" + questionId + '\'' +
                ", contestQuestionId='" + contestQuestionId + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", questionType='" + questionType + '\'' +
                ", questionText='" + questionText + '\'' +
                ", questionUrl='" + questionUrl + '\'' +
                ", optionOne='" + optionOne + '\'' +
                ", optionTwo='" + optionTwo + '\'' +
                ", optionThree='" + optionThree + '\'' +
                ", optionFour='" + optionFour + '\'' +
                ", answerType='" + answerType + '\'' +
                ", points='" + points + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
