package com.cagriturk.servicemanager.dto;

import java.util.List;

/**
 * Data Transfer Object (DTO) representing a service.
 * Contains the service's ID and resources.
 */
public record ServiceDto(
        String id,
        List<ResourceDto> resources
) {
}