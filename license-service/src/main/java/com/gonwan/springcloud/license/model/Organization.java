package com.gonwan.springcloud.license.model;

import org.springframework.data.redis.core.RedisHash;

/*
 * # SMEMBERS organization
 * # HGETALL organization:e254f8c-c442-4ebe-a82a-e2fc1d1ff78a
 */
@RedisHash("organization")
public class Organization {

    private String id;

    private String name;

    private String contactName;

    private String contactEmail;

    private String contactPhone;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

}