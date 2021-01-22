package com.example.demo.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

@Slf4j
public class HttpUtils {

    public static String get(String url, Map<String, String> headers) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> header = new HttpHeaders();
        headers.forEach(header::add);
        URI uri = new URI(url);
        RequestEntity requestEntity = new RequestEntity<>(header, HttpMethod.GET, uri);
        ResponseEntity<String> exchange = restTemplate.exchange(requestEntity, String.class);

        if (exchange.getStatusCode().equals(HttpStatus.OK)) {
            log.info("call {} success", url);
        }

        return exchange.getBody();
    }

}