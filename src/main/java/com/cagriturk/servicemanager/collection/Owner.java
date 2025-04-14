package com.cagriturk.servicemanager.collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
}
