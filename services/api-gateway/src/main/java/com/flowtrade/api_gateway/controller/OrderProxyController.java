package com.flowtrade.api_gateway.controller;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flowtrade.api_gateway.proxy.OrderProxy;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/order/**")
public class OrderProxyController {

    private final OrderProxy proxy;

    public OrderProxyController(OrderProxy proxy) {
        this.proxy = proxy;
    }

    @RequestMapping("/**")
    public ResponseEntity<String> proxy(HttpServletRequest request)
            throws Exception {

        String path = request.getRequestURI();

        HttpResponse<String> response = proxy.forward(request, path);

        return ResponseEntity
                .status(response.statusCode())
                .body(response.body());
    }

}
