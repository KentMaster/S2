package com.zhou.stockindicator.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.zhou.stockindicator.security.AuthoritiesConstants;
import com.zhou.stockindicator.service.YahooStockInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import java.io.IOException;

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
        yahooStockInfoService.getStockData(symbol);
//        return Optional.ofNullable(userRepository.findOne(login))
//                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
//                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
