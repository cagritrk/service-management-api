package com.cagriturk.servicemanager.collection;

import com.cagriturk.servicemanager.dto.OwnerDto;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

/**
 * Represents an owner entity stored in the MongoDB collection "owner".
 * Contains the owner's ID, name, account number, and level.
 */
@Document(collection = "owner")
public record Owner(
        @Id String id,
        String name,
        String accountNumber,
        Integer level) {

    /**
     * Creates a new Owner instance from the given OwnerDto.
     * @param dto the OwnerDto to convert
     * @return a new Owner instance, or null if dto is null
     */
    public static Owner fromDto(OwnerDto dto) {
        if (dto == null) return null;
        String id = Optional.ofNullable(dto.id())
                .filter(Predicate.not(String::isBlank))
                .orElseGet(() -> UUID.randomUUID().toString());
        return new Owner(id, dto.name(), dto.accountNumber(), dto.level());
    }

    /**
     * Converts this Owner instance to an OwnerDto.
     * @return a new OwnerDto instance representing this Owner
     */
    public OwnerDto toDto() {
        return new OwnerDto(this.id, this.name, this.accountNumber, this.level);
    }
}
