package io.foinse.dynamicapi.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.Document;
import org.springframework.data.annotation.Id;

import java.io.IOException;
import java.util.Map;

public class GenericResource {
    @Id
    public int id;

    public String name;

    public Object properties;

    public GenericResource() {}

    public GenericResource(String name, Map<String, Object> properties) {
        this.name = name;
        this.properties = properties;
    }

    public Document toBsonDocument() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(this.properties);
        Document document = new Document();
        return Document.parse(jsonString);
    }
}
