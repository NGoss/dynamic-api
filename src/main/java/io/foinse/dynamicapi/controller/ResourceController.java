package io.foinse.dynamicapi.controller;

import io.foinse.dynamicapi.model.GenericResource;
import io.foinse.dynamicapi.repository.IMongoRepository;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class ResourceController {
    private IMongoRepository mongoRepository;

    @Autowired
    public ResourceController(IMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @RequestMapping(method=RequestMethod.OPTIONS, path="/")
    public ResponseEntity handleOptions() {
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(method=RequestMethod.GET, path="/")
    public ResponseEntity<ArrayList<String>> getAllResourceDefinitions() {
        return new ResponseEntity<>(mongoRepository.getAllCollectionNames(), HttpStatus.OK);
    }

    @RequestMapping(method=RequestMethod.OPTIONS, path="/{resourceName}")
    public ResponseEntity handleResourceOptions() {
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(method=RequestMethod.POST, path = "/{resourceName}")
    public ResponseEntity createResourceDefinition(@PathVariable String resourceName,
                                                   @RequestBody Map<String, Object> resource) {
        mongoRepository.createResource(new GenericResource(resourceName, resource));

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @RequestMapping(method=RequestMethod.GET, path = "/{resourceName}")
    public ResponseEntity<ArrayList<Document>> getAllResourcesForType(@PathVariable String resourceName) {
        ArrayList<Document> resources = mongoRepository.getAllForCollection(resourceName);

        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @RequestMapping(method=RequestMethod.GET, path = "/{resourceName}/{id}")
    public ResponseEntity<Document> getResourceWithId(@PathVariable String resourceName, @PathVariable String id) {
        Document resource = mongoRepository.getSingleDocumentFromCollection(resourceName, id);

        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @RequestMapping(method=RequestMethod.PUT, path = "/{resourceName}/{id}")
    public ResponseEntity<String> updateResourceWithId(@PathVariable String resourceName, @PathVariable String id,
                                                       @RequestBody Map<String, Object> resource) {
        try {
            mongoRepository.updateDocumentById(id, new GenericResource(resourceName, resource));
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method=RequestMethod.DELETE, path = "/{resourceName}/{id}")
    public ResponseEntity deleteResourceById(@PathVariable String resourceName, @PathVariable String id) {
        mongoRepository.deleteDocumentById(resourceName, id);

        return new ResponseEntity(HttpStatus.OK);
    }
}
