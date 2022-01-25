package com.gonwan.springcloud.license.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LicenseRepository extends JpaRepository<License, String> {

    List<License> findByOrganizationId(String organizationId);

    License findByIdAndOrganizationId(String id, String organizationId);

}
