package com.httpconvertor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

//@RestController
public class Controller {
    private final RestTemplate restTemplate;
    public Controller(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Value("#{'http://${core.amber.host}:${core.amber.port}'}")
    private URI amberURI;

//    @PostMapping(value = "/")
    public Object multuPart () throws IOException {
        ResponseEntity<MultiValueMap> exchange = restTemplate.exchange(amberURI.resolve("/encrypt"),
                HttpMethod.POST,
                null,
                new ParameterizedTypeReference<MultiValueMap>() {
                });
        File tmp = File.createTempFile("tmp", "");
       return exchange.getBody().get("data");
    }
}
