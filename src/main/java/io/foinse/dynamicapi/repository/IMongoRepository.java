package io.foinse.dynamicapi.repository;

import io.foinse.dynamicapi.model.GenericResource;
import org.bson.Document;

import java.util.List;

public interface IMongoRepository {
    void createResource(GenericResource resource);
    List<Document> getAllForCollection(String collectionName);
}
