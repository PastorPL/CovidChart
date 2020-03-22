package com.pixelfreak.repository;

import com.pixelfreak.domain.Entry;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data MongoDB repository for the Entry entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EntryRepository extends MongoRepository<Entry, String> {

    Entry findFirstByOrderByLastUpdateDesc();

    List<Entry> findByCountry(String country);

}
