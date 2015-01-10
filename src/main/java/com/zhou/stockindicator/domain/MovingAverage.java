package com.zhou.stockindicator.domain;

/**
 * Created by lizhou on 1/6/15.
 */
public abstract class MovingAverage {
    private String date;
    private String symbol;
    private double movingAverage;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getMovingAverage() {
        return movingAverage;
    }

    public void setMovingAverage(double movingAverage) {
        this.movingAverage = movingAverage;
    }
}
