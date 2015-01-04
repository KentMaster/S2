package com.zhou.stockindicator.service;

import com.zhou.stockindicator.domain.stock.StockInfo;
import com.zhou.stockindicator.repository.StockInfoRepository;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class YahooStockInfoService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);
    @Inject
    private StockInfoRepository stockInfoRepository;

    public void getStockDataFromYahoo(String symbol) throws IOException {
        File f = new File(symbol + ".csv");
        if(!f.exists()) {
            FileUtils.copyURLToFile(
                    new URL(String.format("http://real-chart.finance.yahoo.com/table.csv?s=%s&d=11&e=21&f=2014&g=d&a=11&b=12&c=2013&ignore=.csv", symbol)),
                    new File(symbol + ".csv")
            );
        }
        readRecords(symbol);
    }

    public List<StockInfo> getStockInfo(String symbol, int pageNumber){
        Pageable pageable = new PageRequest(pageNumber, 20, new Sort(Sort.Direction.DESC, "date"));
        return  stockInfoRepository.getBySymbol(symbol,pageable).getContent();
    }

    public List<StockInfo> readRecords(String symbol) throws IOException {
        Function<String, StockInfo> csvToStockInfo = line -> save(line, symbol);
        Path path = Paths.get(symbol + ".csv");
        List<StockInfo> collect = Files
                .lines(path)
                .parallel()
                .skip(1)
                .map(csvToStockInfo)
                .collect(Collectors.toList());
        return collect;
    }

    public StockInfo save(String input, String symbol) {
        String[] line = input.split(",");
        StockInfo stockInfo = new StockInfo(
                line[0],
                symbol,
                Double.parseDouble(line[1]),
                Double.parseDouble(line[2]),
                Double.parseDouble(line[3]),
                Double.parseDouble(line[4]),
                new BigInteger(line[5]),
                Double.parseDouble(line[6]));
        stockInfoRepository.save(stockInfo);
        return stockInfo;
    }

}
