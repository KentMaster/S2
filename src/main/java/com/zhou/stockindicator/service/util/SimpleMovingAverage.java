package com.zhou.stockindicator.service.util;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Created by li on 1/19/15.
 */
public class SimpleMovingAverage {
    public static Queue<Double> data = new ArrayDeque<>();

    public double getMovingAverage(){

        if(data.size()<10){
            return 0.0;
        }

        return Double.parseDouble(null);
    }
}
