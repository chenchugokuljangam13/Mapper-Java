package org.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

import java.util.Map;

@Service
public class FinalMapperService {
    
    private final RestTemplate restTemplate;
    
    @Value("${api.base.url:https://api.dev.curantissolutions.com}")
    private String apiBaseUrl;
    
    @Value("${api.auth.token:}")
    private String authToken;
    
    @Value("${api.auth.mode:}")
    private String authMode;
    
    public FinalMapperService() {
        this.restTemplate = new RestTemplate();
    }
    
    public Map<String, Object> getTempFromAPI(String templateName) {
        try {
            String url = apiBaseUrl + "/visits/" + "15990" + "/template/" + templateName;
            
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authToken);
            headers.set("x-auth-mode", authMode);
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            Map<String, Object> responseBody = response.getBody();
            
            if (responseBody != null && responseBody.containsKey("tmplJson")) {
                Map<String, Object> tmplJson = (Map<String, Object>) responseBody.get("tmplJson");
                if (tmplJson != null && tmplJson.containsKey("value")) {
                    String jsonValue = (String) tmplJson.get("value");
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        return objectMapper.readValue(jsonValue, Map.class);
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to parse tmplJson.value", e);
                    }
                }
            }
            
            throw new RuntimeException("tmplJson.value not found in response");
        } catch (RestClientException e) {
            throw new RuntimeException("Failed to fetch template: " + templateName, e);
        }
    }
}
