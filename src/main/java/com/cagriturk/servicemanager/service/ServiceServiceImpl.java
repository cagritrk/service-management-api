package com.cagriturk.servicemanager.service;

import com.cagriturk.servicemanager.collection.Resource;
import com.cagriturk.servicemanager.collection.Service;
import com.cagriturk.servicemanager.dto.ServiceDto;
import com.cagriturk.servicemanager.dto.ServiceSummaryDto;
import com.cagriturk.servicemanager.exception.NotFoundException;
import com.cagriturk.servicemanager.repository.ServiceRepository;
import org.springframework.cache.annotation.*;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link ServiceService} interface.
 * Provides business logic for managing services.
 */
@CacheConfig(cacheNames = "services")
@org.springframework.stereotype.Service
public class ServiceServiceImpl implements ServiceService {

    private final ServiceRepository serviceRepository;

    /**
     * Constructor for ServiceServiceImpl.
     * @param serviceRepository the repository for service data access
     */
    public ServiceServiceImpl(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable(key = "'service::' + #id")
    public Optional<ServiceDto> findById(String id) {
        return serviceRepository.findById(id).map(Service::toDto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Caching(
            evict = @CacheEvict(key = "'serviceSummaries'"),
            put = @CachePut(key = "'service::' + #result.id")
    )
    public ServiceDto save(ServiceDto serviceDto) {
        Service service = Service.fromDto(serviceDto);
        Service savedService = serviceRepository.save(service);
        return savedService.toDto();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Caching(
            evict = @CacheEvict(key = "'serviceSummaries'"),
            put = @CachePut(key = "'service::' + #result.id")
    )
    @Retryable(retryFor = OptimisticLockingFailureException.class, backoff = @Backoff(delay = 100))
    public ServiceDto update(String id, ServiceDto serviceDto) throws NotFoundException {
        Long currentVersion = serviceRepository.findById(id)
                .map(Service::version)
                .orElseThrow(() -> new NotFoundException("Service", "id", id));

        List<Resource> resources = serviceDto.resources().stream()
                .map(Resource::fromDto)
                .collect(Collectors.toList());

        Service updatedService = serviceRepository.findAndUpdateResourcesWithVersion(
                id,
                resources,
                currentVersion);

        if (updatedService == null) {
            throw new OptimisticLockingFailureException("Concurrent modification detected or service not found with specified version");
        }

        return updatedService.toDto();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Caching(evict = {
            @CacheEvict(key = "'service::' + #id"),
            @CacheEvict(key = "'serviceSummaries'")
    })
    public void deleteById(String id) throws NotFoundException {
        serviceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Service", "id", id));
        serviceRepository.deleteById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable(key = "'serviceSummaries'")
    public List<ServiceSummaryDto> findAllServiceSummaries() {
        return serviceRepository.findAllServiceSummaries();
    }

}