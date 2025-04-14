package com.cagriturk.servicemanager.repository;

import com.cagriturk.servicemanager.collection.Resource;
import com.cagriturk.servicemanager.collection.Service;
import com.cagriturk.servicemanager.dto.ServiceSummaryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

/**
 * Implementation of custom repository methods for Service operations using MongoTemplate.
 */
@Repository
public class ServiceRepositoryImpl implements ServiceRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public ServiceRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Service findAndUpdateResourcesWithVersion(String id, List<Resource> resources, Long version) {
        Query query = new Query(Criteria.where("_id").is(id).and("version").is(version));

        Update update = new Update()
                .set("resources", resources)
                .inc("version", 1);

        FindAndModifyOptions options = FindAndModifyOptions.options().returnNew(true);

        return mongoTemplate.findAndModify(query, update, options, Service.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ServiceSummaryDto> findAllServiceSummaries() {
        AggregationExpression resourcesOrDefault = ConditionalOperators.ifNull("resources").then(Collections.emptyList());

        ProjectionOperation projectStage = Aggregation.project("id").and(ArrayOperators.Size.lengthOfArray(resourcesOrDefault)).as("resourceCount");

        Aggregation aggregation = Aggregation.newAggregation(projectStage);

        return mongoTemplate.aggregate(aggregation, mongoTemplate.getCollectionName(Service.class), ServiceSummaryDto.class)
                .getMappedResults();
    }
}