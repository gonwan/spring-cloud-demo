package com.gonwan.springcloud.organization.controller;

import com.gonwan.springcloud.organization.model.Organization;
import com.gonwan.springcloud.organization.service.OrganizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Organization Controller", description = "Defines organization operations")
@RestController
@RequestMapping("/v1/organizations")
public class OrganizationServiceController {

    private static final Logger logger = LoggerFactory.getLogger(OrganizationServiceController.class);

    @Autowired
    private OrganizationService organizationService;

    /*
     * # curl http://localhost:8085/v1/organizations/e254f8c-c442-4ebe-a82a-e2fc1d1ff78a
     * # curl -u eagleeye:thisissecret http://localhost:8901/oauth/token -X POST -d "grant_type=password&scope=webclient&username=user&password=password1"
     * {"access_token":"08b6eca8-010a-4c6b-a421-f1d5d02a6061","token_type":"bearer","refresh_token":"e67bf15c-f3d8-4092-8eff-0ddbf00984f0","expires_in":43199,"scope":"webclient"}
     * # curl -H "Authorization: Bearer 08b6eca8-010a-4c6b-a421-f1d5d02a6061" http://localhost:8085/v1/organizations/e254f8c-c442-4ebe-a82a-e2fc1d1ff78a
     */
    @Operation(description = "Get organization by ID")
    @Parameter(name = "Authorization", in = ParameterIn.HEADER, required = true, schema = @Schema(implementation = String.class, example = "Bearer <access_token>"))
    @GetMapping("/{id}")
    public Organization getOrganization(@PathVariable("id") String id) {
        logger.debug("Looking up data for organization: {}", id);
        return organizationService.get(id);
    }

    /*
     * # curl -H "Authorization: Bearer 08b6eca8-010a-4c6b-a421-f1d5d02a6061" -H "Content-Type: application/json" http://localhost:8085/v1/organizations \
     *        -X POST -d '{ "name": "name1", "contactName": "contactName1", "contactEmail": "contactEmail1", "contactPhone": "123456" }'
     */
    @Operation(description = "Save organization")
    @Parameter(name = "Authorization", in = ParameterIn.HEADER, required = true, schema = @Schema(implementation = String.class, example = "Bearer <access_token>"))
    @PostMapping
    public void saveOrganization(@RequestBody Organization org) {
        organizationService.save(org);
    }

    /*
     * # curl -H "Authorization: Bearer 08b6eca8-010a-4c6b-a421-f1d5d02a6061" -H "Content-Type: application/json" http://localhost:8085/v1/organizations/4479bbed-8a3d-42f1-99d6-1c0c747dc381 \
     *        -X PUT -d '{ "name": "name2", "contactName": "contactName2", "contactEmail": "contactEmail2", "contactPhone": "654321" }'
     */
    @Operation(description = "Update organization by ID")
    @Parameter(name = "Authorization", in = ParameterIn.HEADER, required = true, schema = @Schema(implementation = String.class, example = "Bearer <access_token>"))
    @PutMapping("/{id}")
    public void updateOrganization(@PathVariable("id") String id, @RequestBody Organization org) {
        organizationService.update(id, org);
    }

    /*
     * # curl -H "Authorization: Bearer 08b6eca8-010a-4c6b-a421-f1d5d02a6061" -H "Content-Type: application/json" http://localhost:8085/v1/organizations/4479bbed-8a3d-42f1-99d6-1c0c747dc381 -X DELETE
     */
    @Operation(description = "Delete organization by ID")
    @Parameter(name = "Authorization", in = ParameterIn.HEADER, required = true, schema = @Schema(implementation = String.class, example = "Bearer <access_token>"))
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrganization(@PathVariable("id") String id) {
        organizationService.delete(id);
    }

}
