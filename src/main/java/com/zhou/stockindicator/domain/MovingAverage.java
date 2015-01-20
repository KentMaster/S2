package com.zhou.stockindicator.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;

/**
 * Created by lizhou on 1/6/15.
 */
@Document
public abstract class MovingAverage {

    @Id
    private BigInteger id;
    private String date;
    private String symbol;
    private double movingAverage;

    public MovingAverage(String date, String symbol, double movingAverage) {
        this.date = date;
        this.symbol = symbol;
        this.movingAverage = movingAverage;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

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
