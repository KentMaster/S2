package com.zhou.stockindicator.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.zhou.stockindicator.service.YahooStockInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.inject.Inject;
import java.io.IOException;

/**
 * Created by lizhou on 1/2/15.
 */
public class YahooStockInfoResource {
    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    @Inject
    private YahooStockInfoService yahooStockInfoService;

    /**
     * GET  /rest/stock/:symbol -> get the Stock Symbol.
     */
    @RequestMapping(value = "/rest/stock/{symbol}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void getStockSymbol(@RequestParam(value = "symbol") String symbol) throws IOException {
        log.debug("REST request to get Stock : {}", symbol);
        yahooStockInfoService.getStockData(symbol);
//        return Optional.ofNullable(userRepository.findOne(login))
//                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
//                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
