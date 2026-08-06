package com.flowtrade.api_gateway.proxy;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class OrderProxy {

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Value("${order-service-base-url}")
    private String baseUrl;

    public HttpResponse<String> forward(HttpServletRequest request, String path)
            throws IOException, InterruptedException {

        HttpRequest proxyRequest = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + path))
                .method(
                    request.getMethod(),
                    HttpRequest.BodyPublishers.noBody()
                )
                .build();

        return httpClient.send(
                proxyRequest,
                HttpResponse.BodyHandlers.ofString()
        );
    }
}
