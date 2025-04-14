package com.cagriturk.servicemanager.collection;

import com.cagriturk.servicemanager.dto.OwnerDto;
import com.cagriturk.servicemanager.dto.ResourceDto;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Represents a resource entity stored in the MongoDB collection "resource".
 * Contains the resource's ID and owners.
 */
@Document(collection = "resource")
public record Resource(
        @Id String id,
        List<Owner> owners) {

    /**
     * Creates a new Resource instance from the given ResourceDto.
     * @param dto the ResourceDto to convert
     * @return a new Resource instance, or null if dto is null
     */
    public static Resource fromDto(ResourceDto dto) {
        if (dto == null) return null;
        String id = Optional.ofNullable(dto.id())
                .filter(Predicate.not(String::isBlank))
                .orElseGet(() -> UUID.randomUUID().toString());
        List<Owner> ownerDocs = Optional.ofNullable(dto.owners())
                .orElse(Collections.emptyList())
                .stream()
                .map(Owner::fromDto)
                .collect(Collectors.toList());
        return new Resource(id, ownerDocs);
    }

    /**
     * Converts this Resource instance to a ResourceDto.
     * @return a new ResourceDto instance representing this Resource
     */
    public ResourceDto toDto() {
        List<OwnerDto> ownerDtos = Optional.ofNullable(this.owners)
                .orElse(Collections.emptyList())
                .stream()
                .map(Owner::toDto)
                .collect(Collectors.toList());
        return new ResourceDto(this.id, ownerDtos);
    }
}

