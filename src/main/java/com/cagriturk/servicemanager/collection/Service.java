package com.cagriturk.servicemanager.collection;

import com.cagriturk.servicemanager.dto.ResourceDto;
import com.cagriturk.servicemanager.dto.ServiceDto;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

/**
 * Represents a service entity stored in the MongoDB collection "service".
 * Contains the service's ID, resources, and version for optimistic locking.
 */
@Document(collection = "service")
public record Service(
        @Id String id,
        List<Resource> resources,
        @Version Long version) {

    /**
     * Creates a new Service instance from the given ServiceDto.
     * @param dto the ServiceDto to convert
     * @return a new Service instance, or null if dto is null
     */
    public static Service fromDto(ServiceDto dto) {
        if (dto == null) return null;
        String id = Optional.ofNullable(dto.id())
                .filter(Predicate.not(String::isBlank))
                .orElseGet(() -> UUID.randomUUID().toString());
        List<Resource> resourceDocs = Optional.ofNullable(dto.resources())
                .orElse(Collections.emptyList())
                .stream()
                .map(Resource::fromDto)
                .toList();
        return new Service(id, resourceDocs, null);
    }

    /**
     * Converts this Service instance to a ServiceDto.
     * @return a new ServiceDto instance representing this Service
     */
    public ServiceDto toDto() {
        List<ResourceDto> resourceDtos = resources.stream()
                .map(Resource::toDto)
                .toList();
        return new ServiceDto(id, resourceDtos);
    }
}
