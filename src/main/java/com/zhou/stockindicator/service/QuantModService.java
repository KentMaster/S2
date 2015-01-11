package com.zhou.stockindicator.service;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

/**
 * Created by lizhou on 1/10/15.
 */
public class QuantModService {

    public static void main(String[] arg) throws RserveException {
        RConnection rConnection = new RConnection();
        REXP x = rConnection.eval("library(quantmod)");
    }
}
