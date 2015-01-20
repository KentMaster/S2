package com.zhou.stockindicator.service;

import com.zhou.stockindicator.domain.FiftyDayMovingAverge;
import com.zhou.stockindicator.domain.TwoHundredDayMovingAverage;
import com.zhou.stockindicator.domain.stock.StockInfo;
import com.zhou.stockindicator.repository.FiftyDayMovingAverageRepository;
import com.zhou.stockindicator.repository.StockInfoRepository;
import com.zhou.stockindicator.repository.TwoHundredDayMovingAverageRepository;
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

    private final Logger log = LoggerFactory.getLogger(YahooStockInfoService.class);
    @Inject
    private StockInfoRepository stockInfoRepository;
    @Inject
    private FiftyDayMovingAverageRepository fiftyDayMovingAverageRepository;
    @Inject
    private TwoHundredDayMovingAverageRepository twoHundredDayMovingAverageRepository;

    public void getStockDataFromYahoo(String symbol) throws IOException {
        log.debug("***Symbol***"+symbol);
        File f = new File(symbol + ".csv");
        if(!f.exists()) {
            FileUtils.copyURLToFile(
                    new URL(String.format("http://real-chart.finance.yahoo.com/table.csv?s=%s&d=11&e=21&f=2014&g=d&a=11&b=12&c=2013&ignore=.csv", symbol)),
                    new File(symbol + ".csv")
            );
        }
        List<StockInfo> stockInfos1 = readRecords(symbol);
        Pageable firstFifty = new PageRequest(0, 50, new Sort(Sort.Direction.DESC, "date"));
        List<StockInfo> stockInfos = stockInfoRepository.getBySymbol(symbol,firstFifty).getContent();
        double sum = stockInfos.stream().mapToDouble(StockInfo::getAdjustedClose).sum()/50;
        fiftyDayMovingAverageRepository.save(new FiftyDayMovingAverge(stockInfos1.get(49).getDate(), symbol, sum));
//        for(int i = 51; i < stockInfos1.size(); i++){
//            StockInfo stockInfo = stockInfoRepository.getBySymbol(symbol, new PageRequest(i, 1, new Sort(Sort.Direction.DESC, "date"))).getContent().get(0);
//            stockInfos.remove(0);
//            stockInfos.add(stockInfo);
//            double sum2 = stockInfos.stream().mapToDouble(StockInfo::getAdjustedClose).sum();
//            fiftyDayMovingAverageRepository.save(new FiftyDayMovingAverge(stockInfos1.get(i).getDate(), symbol, sum2));
//        }

        Pageable firstTwoHundred = new PageRequest(0, 200, new Sort(Sort.Direction.DESC, "date"));
        List<StockInfo> stockInfos2 = stockInfoRepository.getBySymbol(symbol,firstTwoHundred).getContent();
        double sum3 = stockInfos2.stream().mapToDouble(StockInfo::getAdjustedClose).sum();
        twoHundredDayMovingAverageRepository.save(new TwoHundredDayMovingAverage(stockInfos1.get(199).getDate(), symbol, sum3));
//        for(int i = 201; i < stockInfos1.size(); i++){
//            StockInfo stockInfo = stockInfoRepository.getBySymbol(symbol, new PageRequest(i, 1, new Sort(Sort.Direction.DESC, "date"))).getContent().get(0);
//            stockInfos2.remove(0);
//            stockInfos2.add(stockInfo);
//            double sum2 = stockInfos2.stream().mapToDouble(StockInfo::getAdjustedClose).sum();
//            twoHundredDayMovingAverageRepository.save(new TwoHundredDayMovingAverage(stockInfos1.get(i).getDate(), symbol, sum2));
//        }

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
