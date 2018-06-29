package com.example.vedantiladda.quiz.dto;

import java.io.Serializable;

public class QuestionDTO implements Serializable {

    private String questionId;
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
    private String status;
    private Boolean checked = false;
    private String answer;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
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

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "QuestionDTO{" +
                "questionId='" + questionId + '\'' +
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
                ", status='" + status + '\'' +
                '}';
    }

}


