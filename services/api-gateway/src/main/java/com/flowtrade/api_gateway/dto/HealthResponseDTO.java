package com.flowtrade.api_gateway.dto;

import com.flowtrade.api_gateway.constants.HealthStatus;

public class HealthResponseDTO {

    private HealthStatus status;
    private String service;

    public HealthResponseDTO(HealthStatus status, String service){
        this.status = status;
        this.service = service;
    }
    
    public HealthStatus getStatus() {
        return status;
    }
    public void setStatus(HealthStatus status) {
        this.status = status;
    }
    
    public String getService() {
        return service;
    }
    
}
