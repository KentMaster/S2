package com.zhou.stockindicator.repository;

import com.zhou.stockindicator.domain.stock.StockInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface StockInfoRepository extends MongoRepository<StockInfo, String> {

    @Query
    public Page<StockInfo> getBySymbol(String symbol, Pageable pageable);
}
