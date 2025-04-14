package com.cagriturk.servicemanager.collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Represents a resource entity stored in the MongoDB collection "resource".
 * Contains the resource's ID and owners.
 */
@Document(collection = "resource")
public record Resource(
        @Id String id,
        List<Owner> owners) {
}
