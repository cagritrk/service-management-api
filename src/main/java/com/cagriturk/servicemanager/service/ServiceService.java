package com.cagriturk.servicemanager.service;

import com.cagriturk.servicemanager.dto.ServiceDto;
import com.cagriturk.servicemanager.dto.ServiceSummaryDto;
import com.cagriturk.servicemanager.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing services.
 * Provides methods for CRUD operations and retrieving service summaries.
 */
public interface ServiceService {

    /**
     * Retrieves a list of summaries for all services.
     * @return a list of service summaries
     */
    List<ServiceSummaryDto> findAllServiceSummaries();

    /**
     * Retrieves a service by its ID.
     * @param id the ID of the service to retrieve
     * @return an Optional containing the service DTO if found, or an empty Optional if not found
     */
    Optional<ServiceDto> findById(String id);

    /**
     * Saves a new service.
     * @param serviceDto the service DTO to save
     * @return the saved service DTO
     */
    ServiceDto save(ServiceDto serviceDto);

    /**
     * Updates an existing service.
     * @param id the ID of the service to update
     * @param serviceDto the updated service DTO
     * @return the updated service DTO
     */
    ServiceDto update(String id, ServiceDto serviceDto) throws NotFoundException;

    /**
     * Deletes a service by its ID.
     * @param id the ID of the service to delete
     */
    void deleteById(String id) throws NotFoundException;
}
