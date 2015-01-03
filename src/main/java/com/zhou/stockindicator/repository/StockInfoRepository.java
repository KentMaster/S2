package com.zhou.stockindicator.repository;

import com.zhou.stockindicator.domain.stock.StockInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StockInfoRepository extends MongoRepository<StockInfo, String> {
}
