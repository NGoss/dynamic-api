package io.foinse.dynamicapi.repository;

import com.mongodb.MongoClient;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import io.foinse.dynamicapi.model.GenericResource;
import io.foinse.dynamicapi.model.IConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.BsonInvalidOperationException;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;

import static com.mongodb.client.model.Filters.eq;

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

    public ArrayList<Document> getAllForCollection(String collectionName) {
        MongoCollection<Document> collection = mongoDb.getCollection(collectionName);
        FindIterable<Document> documentIterable = collection.find();

        ArrayList<Document> resources = new ArrayList<>();

        for (Document document : documentIterable) {
            ObjectId id = document.get("_id", ObjectId.class);
            document.remove("_id");
            document.append("id", id.toHexString());
            resources.add(document);
        }

        return resources;
    }

    public Document getSingleDocumentFromCollection(String collectionName, String id) {
        MongoCollection<Document> collection = mongoDb.getCollection(collectionName);

        FindIterable<Document> results = collection.find(eq("_id", new ObjectId(id)));
        Document document = results.first();

        if (document != null) {
            ObjectId documentId = document.get("_id", ObjectId.class);
            document.remove("_id");
            document.append("id", documentId.toHexString());
        }

        return document;
    }

    public void updateDocumentById(String id, GenericResource resource) throws IOException {
        MongoCollection<Document> collection = mongoDb.getCollection(resource.name);

        collection.replaceOne(eq("_id", new ObjectId(id)), resource.toBsonDocument());
    }

    public void deleteDocumentById(String collectionName, String id) {
        MongoCollection<Document> collection = mongoDb.getCollection(collectionName);

        collection.deleteOne(eq("_id", new ObjectId(id)));
    }

    public ArrayList<String> getAllCollectionNames() {
        MongoIterable<String> collections = mongoDb.listCollectionNames();
        ArrayList<String> collectionsList = new ArrayList<>();
        return collections.into(collectionsList);
    }
}
