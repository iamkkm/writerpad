package com.xebia.fs101.writerpad.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ImageRenderService {

    public String imageRender() {
        RestTemplate restTemplate = new RestTemplate();
        String template = restTemplate.getForObject(
                "https://api.unsplash.com/photos/random?client_id="
                        + "4f2756addb5fe5c9469c2ef70f81a3466711a0efe61c8630329b91bbb781545b",
                String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(template);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        String imageUrl = null;
        if (jsonNode != null) {
            imageUrl = jsonNode.get("urls").get("small").toString();
        }
        return imageUrl;
    }
}
