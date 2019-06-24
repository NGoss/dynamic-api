package io.foinse.dynamicapi.repository;

import io.foinse.dynamicapi.model.GenericResource;
import org.bson.BsonInvalidOperationException;
import org.bson.Document;

import java.io.IOException;
import java.util.ArrayList;

public interface IMongoRepository {
    void createResource(GenericResource resource);
    ArrayList<Document> getAllForCollection(String collectionName);
    Document getSingleDocumentFromCollection(String collectionName, String id);
    void updateDocumentById(String id, GenericResource resource) throws IOException;
    void deleteDocumentById(String collectionName, String id);
}
