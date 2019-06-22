package io.foinse.dynamicapi.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Configuration implements IConfiguration {
    @Value("${environments.mongodb.dbUrl}")
    private String mongoDbUrl;

    @Value("${environments.mongodb.dbName}")
    private String mongoDbName;

    public String getMongoDbName() {
        return mongoDbName;
    }

    public String getMongoDbUrl() {
        return mongoDbUrl;
    }
}
