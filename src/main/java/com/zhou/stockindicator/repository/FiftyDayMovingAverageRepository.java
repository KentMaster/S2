package com.zhou.stockindicator.repository;

import com.zhou.stockindicator.domain.FiftyDayMovingAverge;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by li on 1/19/15.
 */
public interface FiftyDayMovingAverageRepository extends MongoRepository<FiftyDayMovingAverge, String> {
}
