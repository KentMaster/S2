package com.zhou.stockindicator.domain;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by li on 1/19/15.
 */
public class FiftyDayMovingAverge extends MovingAverage {
    public FiftyDayMovingAverge(String date, String symbol, double movingAverage) {
        super(date, symbol, movingAverage);
    }
}
