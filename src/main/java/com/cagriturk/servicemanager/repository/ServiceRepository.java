package com.cagriturk.servicemanager.repository;

import com.cagriturk.servicemanager.collection.Service;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Service entities.
 * Extends MongoRepository to provide CRUD operations for Service documents.
 * Also includes custom repository functionality through ServiceRepositoryCustom.
 */
@Repository
public interface ServiceRepository extends MongoRepository<Service, String>, ServiceRepositoryCustom {
}
