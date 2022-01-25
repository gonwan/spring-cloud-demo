package com.gonwan.springcloud.license.controller;

import com.gonwan.springcloud.license.service.DiscoveryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Tool Controller", description = "Defines tool operations")
@RestController
@RequestMapping("/v1/tools")
public class ToolController {

    @Autowired
    private DiscoveryService discoveryService;

    @Operation(description = "Get eureka services")
    @Parameter(name = "Authorization", in = ParameterIn.HEADER, required = true, schema = @Schema(implementation = String.class, example = "Bearer <access_token>"))
    @GetMapping("/eureka/services")
    public List<String> getEurekaServices() {
        return discoveryService.getEurekaServices();
    }

}
