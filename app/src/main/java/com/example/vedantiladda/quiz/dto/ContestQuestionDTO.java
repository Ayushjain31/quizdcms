package com.example.vedantiladda.quiz.dto;

public class ContestQuestionDTO {
    private ContestDTO contestDTO;
    private String questionId;
    private Integer points;
    private Integer visibleTime;
    private Integer sequence;
    private Boolean visible;
    private String contestQuestionId;
    private QuestionDTO questionDTO;
    private UserAnswerDTO userAnswerDTO;


    @Override
    public String toString() {
        return "ContestQuestionDTO{" +
                "contestDTO=" + contestDTO +
                ", questionId='" + questionId + '\'' +
                ", points=" + points +
                ", visibleTime=" + visibleTime +
                ", sequence=" + sequence +
                ", visible=" + visible +
                ", contestQuestionId='" + contestQuestionId + '\'' +
                ", questionDTO=" + questionDTO +
                ", userAnswerDTO=" + userAnswerDTO +
                '}';
    }

    public ContestDTO getContestDTO() {
        return contestDTO;
    }

    public void setContestDTO(ContestDTO contestDTO) {
        this.contestDTO = contestDTO;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getVisibleTime() {
        return visibleTime;
    }

    public void setVisibleTime(Integer visibleTime) {
        this.visibleTime = visibleTime;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }


    public String getContestQuestionId() {
        return contestQuestionId;
    }

    public void setContestQuestionId(String contestQuestionId) {
        this.contestQuestionId = contestQuestionId;
    }

    public QuestionDTO getQuestionDTO() {
        return questionDTO;
    }

    public void setQuestionDTO(QuestionDTO questionDTO) {
        this.questionDTO = questionDTO;
    }

    public UserAnswerDTO getUserAnswerDTO() {
        return userAnswerDTO;
    }

    public void setUserAnswerDTO(UserAnswerDTO userAnswerDTO) {
        this.userAnswerDTO = userAnswerDTO;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

}