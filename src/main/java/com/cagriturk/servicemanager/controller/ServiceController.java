package com.cagriturk.servicemanager.controller;

import com.cagriturk.servicemanager.dto.ServiceDto;
import com.cagriturk.servicemanager.dto.ServiceSummaryDto;
import com.cagriturk.servicemanager.service.ServiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing services.
 */
@RestController
@RequestMapping("/services")
public class ServiceController {

    private final ServiceService serviceService;

    /**
     * Constructor for ServiceController.
     * @param serviceService the service for business logic
     */
    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @Operation(summary = "Create a new service")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Service created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ServiceDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    @PostMapping
    public ResponseEntity<ServiceDto> create(@RequestBody ServiceDto serviceDto) {
        ServiceDto savedServiceDto = serviceService.save(serviceDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedServiceDto);
    }

    @Operation(summary = "Get all service summaries")
    @ApiResponse(responseCode = "200", description = "List of service summaries",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ServiceSummaryDto.class)))
    @GetMapping
    public ResponseEntity<List<ServiceSummaryDto>> getAllServiceSummaries() {
        List<ServiceSummaryDto> serviceDtos = this.serviceService.findAllServiceSummaries();
        return ResponseEntity.ok(serviceDtos);
    }

    @Operation(summary = "Get a service by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ServiceDto.class))),
            @ApiResponse(responseCode = "404", description = "Service not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ServiceDto> getById(@PathVariable String id) {
        return serviceService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update an existing service")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Service updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ServiceDto.class))),
            @ApiResponse(responseCode = "404", description = "Service not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ServiceDto> update(@PathVariable String id, @RequestBody ServiceDto serviceDto) {
        ServiceDto updatedServiceDto = serviceService.update(id, serviceDto);
        return ResponseEntity.ok(updatedServiceDto);
    }

    @Operation(summary = "Delete a service by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Service deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Service not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        serviceService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
