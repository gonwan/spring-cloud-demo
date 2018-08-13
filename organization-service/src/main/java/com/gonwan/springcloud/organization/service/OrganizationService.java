package com.gonwan.springcloud.organization.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.gonwan.springcloud.organization.model.Organization;
import com.gonwan.springcloud.organization.model.OrganizationRepository;

@Service
public class OrganizationService {

    private static final Logger logger = LoggerFactory.getLogger(OrganizationService.class);

    @Autowired
    private OrganizationRepository repository;

    @Autowired
    private Tracer tracer;

    /* @EnableBinding */
    @Autowired
    private Source source;

    public Organization get(String id) {
        return repository.findById(id);
    }

    public void save(Organization org) {
        org.setId(UUID.randomUUID().toString());
        repository.save(org);
        publish("SAVE", org.getId());
    }

    public void update(String id, Organization org) {
        org.setId(id);
        repository.save(org);
        publish("UPDATE", org.getId());
    }

    public void delete(String id) {
        repository.delete(id);
        publish("DELETE", id);
    }

    private void publish(String action, String id) {
        logger.debug("Sending {} message for organization id: {}", action, id);
        Map<String, String> msg = new HashMap<>();
        msg.put("action", action);
        msg.put("organizationId", id);
        msg.put("correlationId", tracer.getCurrentSpan().traceIdString());
        source.output().send(MessageBuilder.withPayload(msg).build());
    }

}
