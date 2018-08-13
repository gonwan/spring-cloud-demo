package com.gonwan.springcloud.license.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

@Service
public class DiscoveryService {

    @Autowired
    private DiscoveryClient discoveryClient;

    public List<String> getEurekaServices() {
        List<String> services = new ArrayList<>();
        discoveryClient.getServices().forEach(serviceId -> {
            discoveryClient.getInstances(serviceId).forEach(instance -> {
                services.add(String.format("%s:%s", serviceId, instance.getUri()));
            });
        });
        return services;
    }

}
