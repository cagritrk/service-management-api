package com.cagriturk.servicemanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a requested resource is not found.
 * Automatically returns HTTP 404 status code when thrown from a controller.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    /**
     * Constructs a new NotFoundException with details about the missing resource.
     * @param resourceName The type/name of the resource that wasn't found
     * @param fieldName The field that was used to search for the resource
     * @param fieldValue The value that was used in the search
     */
    public NotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
    }
}
