package io.foinse.dynamicapi.repository;

import com.mongodb.MongoClient;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import io.foinse.dynamicapi.model.GenericResource;
import io.foinse.dynamicapi.model.IConfiguration;
import io.foinse.dynamicapi.model.MongoResource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MongoRepository implements IMongoRepository {

    private MongoDatabase mongoDb;
    private Logger logger;

    @Autowired
    public MongoRepository(IConfiguration config) {
        logger = LogManager.getLogger("MongoRepository");

        try {
            MongoClient client = new MongoClient(config.getMongoDbUrl());
            mongoDb = client.getDatabase(config.getMongoDbName());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public void createResource(GenericResource resource) {
        MongoCollection<Document> collection = mongoDb.getCollection(resource.name);

        try {
            collection.insertOne(resource.toBsonDocument());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public ArrayList<MongoResource> getAllForCollection(String collectionName) {
        MongoCollection<Document> collection = mongoDb.getCollection(collectionName);
        FindIterable<Document> documentIterable = collection.find();

        ArrayList<MongoResource> resources = new ArrayList<>();

        for (Document document : documentIterable) {
            MongoResource resource = new MongoResource();
            ObjectId id = document.get("_id", ObjectId.class);
            resource.setId(id.toHexString());
            document.remove("_id");
            resource.setValue(document);
            resources.add(resource);
        }

        return resources;
    }

//    public Document getSingleDocumentFromCollection(String collectionName, int)
}
