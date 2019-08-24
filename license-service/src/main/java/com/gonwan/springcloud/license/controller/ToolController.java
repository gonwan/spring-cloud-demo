package com.gonwan.springcloud.license.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gonwan.springcloud.license.service.DiscoveryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Tool Controller", description = "Defines tool operations")
@RestController
@RequestMapping("/v1/tools")
public class ToolController {

    @Autowired
    private DiscoveryService discoveryService;

    @ApiOperation("Get eureka services")
    @ApiImplicitParam(name = "Authorization", value = "Bearer <access_token>", dataType = "string", paramType = "header", required = true)
    @GetMapping("/eureka/services")
    public List<String> getEurekaServices() {
        return discoveryService.getEurekaServices();
    }

}
