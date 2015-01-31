package com.zhou.stockindicator.service;

import com.zhou.stockindicator.domain.FiftyDayMovingAverge;
import com.zhou.stockindicator.domain.TwoHundredDayMovingAverage;
import com.zhou.stockindicator.domain.stock.StockInfo;
import com.zhou.stockindicator.repository.FiftyDayMovingAverageRepository;
import com.zhou.stockindicator.repository.StockInfoRepository;
import com.zhou.stockindicator.repository.TwoHundredDayMovingAverageRepository;
import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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
        Optional<StockInfo> optionalStartDate = stockInfoRepository.findFirstByOrderByDateDesc();
        DateTime dateTimeNow = DateTime.now();
        String[] startDate;
        if(optionalStartDate.isPresent()){
            startDate = optionalStartDate.get().getDate().split("-");
        }else{
            startDate = String.format("%s-%s-%s",(dateTimeNow.getYear()-2),dateTimeNow.getMonthOfYear()-1,dateTimeNow.getDayOfMonth()).split("-");
        }
        File f = new File(symbol + ".csv");
        if(!f.exists()) {
            FileUtils.copyURLToFile(
                    new URL(String.format(
                            "http://real-chart.finance.yahoo.com/table.csv?s=%s&a=%s&b=%s&c=%s&d=%s&e=%s&f=%s&g=d&ignore=.csv",
                            symbol,
                            Integer.parseInt(startDate[1]),
                            startDate[2],
                            startDate[0],
                            dateTimeNow.getMonthOfYear()-1,
                            dateTimeNow.getDayOfMonth(),
                            dateTimeNow.getYear()
                            )),
                    new File(symbol + ".csv")
            );
        }
        List<StockInfo> stockInfos = readRecords(symbol);
        Comparator<StockInfo> orderByDate = (s1, s2) -> s1.getDate().compareTo(s2.getDate());
        List<StockInfo> stockInfoSortedByDate = stockInfos.stream().sorted(orderByDate).collect(Collectors.toList());
        mapFiftyDayMovingAverage(stockInfoSortedByDate, 50);
        mapTwoHundredDayMovingAverage(stockInfoSortedByDate, 200);
//        List<StockInfo> stockInfos = stockInfoRepository.findAll(new Sort(Sort.Direction.DESC, "date"));
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

    public List<FiftyDayMovingAverge> mapFiftyDayMovingAverage(List<StockInfo> stockInfo, int interval){
            List<FiftyDayMovingAverge> movingAverages = new ArrayList<>();
            for(int i = 0; i < stockInfo.size()-50; i++){
                List<StockInfo> subStockInfos = stockInfo.subList(i, i + interval);
                double average = subStockInfos.stream().parallel().mapToDouble(StockInfo::getClose).sum()/interval;
                FiftyDayMovingAverge  fiftyDayMovingAverge =  new FiftyDayMovingAverge(subStockInfos.get(subStockInfos.size()-1).getDate(), stockInfo.get(interval+i).getSymbol() ,average);
                movingAverages.add(fiftyDayMovingAverge);
                fiftyDayMovingAverageRepository.save(fiftyDayMovingAverge);
            }
            return movingAverages;
    }

    public List<TwoHundredDayMovingAverage> mapTwoHundredDayMovingAverage(List<StockInfo> stockInfo, int interval){

        List<TwoHundredDayMovingAverage> movingAverages = new ArrayList<>();
        for(int i = 0; i <= stockInfo.size()-200; i++){
            List<StockInfo> subStockInfos = stockInfo.subList(i, i + interval);
            double average = subStockInfos.stream().parallel().mapToDouble(StockInfo::getClose).sum()/interval;
            TwoHundredDayMovingAverage twoHundredDayMovingAverage = new TwoHundredDayMovingAverage(subStockInfos.get(subStockInfos.size()-1).getDate(), stockInfo.get(interval+i).getSymbol() ,average);
            movingAverages.add(twoHundredDayMovingAverage);
            twoHundredDayMovingAverageRepository.save(twoHundredDayMovingAverage);
        }
        return movingAverages;

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
