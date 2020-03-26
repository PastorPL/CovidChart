package com.pixelfreak.repository.custom;

import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

public class EntryCustomRepositoryImpl implements EntryCustomRepository {

    private final MongoTemplate mongoTemplate;

    public EntryCustomRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<String> findAllCountryNames() {
        return null;
    }
}
