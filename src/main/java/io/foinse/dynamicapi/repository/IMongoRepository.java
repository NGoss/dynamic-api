package io.foinse.dynamicapi.repository;

import io.foinse.dynamicapi.model.GenericResource;

public interface IMongoRepository {
    void createCollection(GenericResource resource);
}
