package com.cagriturk.servicemanager.dto;

import java.util.List;

/**
 * Data Transfer Object (DTO) representing a resource.
 * Contains the resource's ID and owners.
 */
public record ResourceDto(
        String id,
        List<OwnerDto> owners
) {
}