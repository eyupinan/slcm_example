
package com.kartaca.slcm.core.client;

import java.util.Collections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@PropertySource("file:./src/main/resources/application.properties")
public class WebClientProvider {
    public WebClient client;
     public WebClientProvider(@Value("${entegration.endpoint}") String endpoint){
        client= WebClient.builder()
            .baseUrl(endpoint)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE) 
            .defaultUriVariables(Collections.singletonMap("url", endpoint))
            .build();
        
    }
}
