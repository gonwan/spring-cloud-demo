package com.gonwan.springcloud.license.event;

public class OrganizationMessage {

    private String action;
    private String organizationId;
    private String correlationId;

    public OrganizationMessage() {
    }

    public OrganizationMessage(String action, String organizationId, String correlationId) {
        this.action = action;
        this.organizationId = organizationId;
        this.correlationId = correlationId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    @Override
    public String toString() {
        return "OrganizationMessage{" +
                "action='" + action + '\'' +
                ", organizationId='" + organizationId + '\'' +
                ", correlationId='" + correlationId + '\'' +
                '}';
    }

}
