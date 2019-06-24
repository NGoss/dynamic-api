package io.foinse.dynamicapi.controller;

import io.foinse.dynamicapi.model.GenericResource;
import io.foinse.dynamicapi.model.MongoResource;
import io.foinse.dynamicapi.repository.IMongoRepository;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class ResourceController {
    private IMongoRepository mongoRepository;

    @Autowired
    public ResourceController(IMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @RequestMapping(method=RequestMethod.POST, path = "/{resourceName}")
    public ResponseEntity createResourceDefinition(@PathVariable String resourceName, @RequestBody Map<String, String> resourceDefinition) {
        GenericResource definition = new GenericResource(resourceName, resourceDefinition);
        mongoRepository.createResource(definition);

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @RequestMapping(method=RequestMethod.GET, path = "/{resourceName}")
    public ResponseEntity<ArrayList<MongoResource>> getAllResourcesForType(@PathVariable String resourceName) {
        ArrayList<MongoResource> resources = mongoRepository.getAllForCollection(resourceName);

        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @RequestMapping(method=RequestMethod.GET, path = "/{resourceName}/{id}")
    public ResponseEntity<MongoResource> getResourceWithId(@PathVariable String resourceName, @PathVariable String id) {
        MongoResource resource = mongoRepository.getSingleDocumentFromCollection(resourceName, id);

        return new ResponseEntity<>(resource, HttpStatus.OK);
    }
}
