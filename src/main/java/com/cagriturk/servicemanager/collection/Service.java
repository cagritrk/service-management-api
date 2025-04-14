package com.cagriturk.servicemanager.collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Represents a service entity stored in the MongoDB collection "service".
 * Contains the service's ID and resources
 */
@Document(collection = "service")
public record Service(
        @Id String id,
        List<Resource> resources) {
}
