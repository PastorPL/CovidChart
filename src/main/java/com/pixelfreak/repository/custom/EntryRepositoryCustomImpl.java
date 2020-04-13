package com.pixelfreak.repository.custom;

import com.mongodb.BasicDBObject;
import com.pixelfreak.domain.Entry;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

public class EntryRepositoryCustomImpl implements EntryRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    public EntryRepositoryCustomImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<String> findAllCountryNames() {
        final GroupOperation groupByCountry = group("country");
        final Aggregation aggregation = newAggregation(groupByCountry);
        final List<String> countryNames = this.mongoTemplate.aggregate(aggregation, Entry.class, BasicDBObject.class).getMappedResults().stream().map(item -> item.getString("_id")).collect(Collectors.toList());
        Collections.sort(countryNames);
        return countryNames;
    }

    @Override
    public List<String> findAllProvinceForCountry(String country) {
        final MatchOperation matchStage = Aggregation.match(new Criteria("country").is(country));
        final GroupOperation groupByCountry = group("province");
        final Aggregation aggregation = newAggregation(matchStage, groupByCountry);
        final List<String> countryNames = this.mongoTemplate.aggregate(aggregation, Entry.class, BasicDBObject.class).getMappedResults().stream().map(item -> item.getString("_id")).collect(Collectors.toList());
        Collections.sort(countryNames);
        return countryNames;
    }
}
