package com.flowtrade.order_service.dto;

import com.flowtrade.order_service.constants.HealthStatus;

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
