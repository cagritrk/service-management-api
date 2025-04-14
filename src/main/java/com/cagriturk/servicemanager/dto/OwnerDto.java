package com.cagriturk.servicemanager.dto;

/**
 * Data Transfer Object (DTO) representing an owner.
 * Contains the owner's ID, name, account number, and level.
 */
public record OwnerDto(
        String id,
        String name,
        String accountNumber,
        Integer level
) {
}