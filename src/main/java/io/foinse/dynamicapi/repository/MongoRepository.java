package io.foinse.dynamicapi.repository;

import com.mongodb.MongoClient;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.foinse.dynamicapi.model.GenericResource;
import io.foinse.dynamicapi.model.IConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

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

    public void createCollection(GenericResource resource) {
        MongoCollection collection = mongoDb.getCollection(resource.name);

        try {
            collection.insertOne(resource.toBsonDocument());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
