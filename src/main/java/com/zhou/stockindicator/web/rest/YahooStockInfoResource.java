package com.zhou.stockindicator.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.zhou.stockindicator.domain.stock.StockInfo;
import com.zhou.stockindicator.service.YahooStockInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

/**
 * Created by lizhou on 1/2/15.
 */
@RestController
@RequestMapping("/app")
public class YahooStockInfoResource {
    private final Logger log = LoggerFactory.getLogger(YahooStockInfoResource.class);

    @Inject
    private YahooStockInfoService yahooStockInfoService;

    /**
     * GET  /rest/stock/:symbol -> get the Stock Symbol.
     */
    @RequestMapping(value = "/rest/stock",
            method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Timed
    public void getStockSymbol(@RequestParam(value = "symbol") String symbol) throws IOException {
        log.debug("REST request to get Stock : {}", symbol);
        yahooStockInfoService.getStockDataFromYahoo(symbol);
    }

    /**
     * GET  /rest/stock/:symbol -> get the Stock Symbol.
     */
    @RequestMapping(value = "/rest/stockinfo",
            method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @Timed
    public List<StockInfo> getStockBySymbol( @RequestParam(value = "pagenumber") int pageNumber, @RequestParam(value = "symbol") String symbol) throws IOException {
        log.debug("REST request to get Stock : {}", symbol);
        return yahooStockInfoService.getStockInfo(symbol, pageNumber);
    }


}
