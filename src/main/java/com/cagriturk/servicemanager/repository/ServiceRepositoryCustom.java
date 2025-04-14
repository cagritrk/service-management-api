package com.cagriturk.servicemanager.repository;

import com.cagriturk.servicemanager.collection.Resource;
import com.cagriturk.servicemanager.collection.Service;
import com.cagriturk.servicemanager.dto.ServiceSummaryDto;

import java.util.List;

/**
 * Custom repository methods for Service operations.
 */
public interface ServiceRepositoryCustom {

    /**
     * Atomically finds a service by ID and expected version, updates its resources,
     * increments the version, and returns the updated service document.
     * Uses MongoDB's findAndModify operation.
     *
     * @param id Service ID
     * @param resources New resources list
     * @param version Expected version for optimistic locking
     * @return The updated Service object if found and updated successfully, otherwise null (indicating version mismatch).
     */
    Service findAndUpdateResourcesWithVersion(String id, List<Resource> resources, Long version);

    /**
     * Finds all services but projects only the ID and the count of resources.
     * Uses MongoDB's $size operator to get the array size directly in the query.
     * Maps the result directly to ServiceSummaryDto.
     *
     * @return A list of ServiceSummaryDto containing only id and resourceCount.
     */
    List<ServiceSummaryDto> findAllServiceSummaries();
}