package io.foinse.dynamicapi.repository;

import io.foinse.dynamicapi.model.GenericResource;
import io.foinse.dynamicapi.model.MongoResource;

import java.util.ArrayList;

public interface IMongoRepository {
    void createResource(GenericResource resource);
    ArrayList<MongoResource> getAllForCollection(String collectionName);
    MongoResource getSingleDocumentFromCollection(String collectionName, String id);
}
