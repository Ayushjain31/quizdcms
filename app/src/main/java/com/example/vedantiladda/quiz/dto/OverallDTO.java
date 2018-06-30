package com.example.vedantiladda.quiz.dto;

import java.math.BigInteger;

public class OverallDTO {
    private String userId;
    private BigInteger finalPoints;
    private BigInteger rank;
    private Integer pointInt;
    private String finalPointsString;

    public String getFinalPointsString() {
        return finalPointsString;
    }

    public void setFinalPointsString(String finalPointsString) {
        this.finalPointsString = finalPointsString;
    }

    public BigInteger getFinalPoints() {
        return finalPoints;
    }

    public void setFinalPoints(BigInteger finalPoints) {
        this.finalPoints = finalPoints;

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigInteger getRank() {
        return rank;
    }

    public void setRank(BigInteger rank) {
        this.rank = rank;
    }

    public Integer getPointInt() {
        return pointInt;
    }

    public void setPointInt(Integer pointInt) {
        this.pointInt = pointInt;
    }
}