package com.cagriturk.servicemanager.service;

import com.cagriturk.servicemanager.collection.Owner;
import com.cagriturk.servicemanager.collection.Resource;
import com.cagriturk.servicemanager.collection.Service;
import com.cagriturk.servicemanager.dto.OwnerDto;
import com.cagriturk.servicemanager.dto.ResourceDto;
import com.cagriturk.servicemanager.dto.ServiceDto;
import com.cagriturk.servicemanager.dto.ServiceSummaryDto;
import com.cagriturk.servicemanager.exception.NotFoundException;
import com.cagriturk.servicemanager.repository.ServiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.OptimisticLockingFailureException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link ServiceServiceImpl} class.
 * Ensures that service logic behaves correctly under various scenarios.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ServiceServiceImpl Tests")
class ServiceServiceImplTest {

    @Mock
    private ServiceRepository serviceRepository;

    @InjectMocks
    private ServiceServiceImpl serviceService;

    private Service service;
    private ServiceDto serviceDto;
    private String serviceId;
    private Long version;

    @BeforeEach
    void setUp() {
        serviceId = UUID.randomUUID().toString();
        version = 1L;

        Owner owner = new Owner("ownerId", "Owner Name", "1234567890", 1);
        OwnerDto ownerDto = new OwnerDto("ownerId", "Owner Name", "1234567890", 1);
        Resource resource = new Resource("resourceId", List.of(owner));
        ResourceDto resourceDto = new ResourceDto("resourceId", List.of(ownerDto));

        service = new Service(serviceId, List.of(resource), version);
        serviceDto = new ServiceDto(serviceId, List.of(resourceDto));
    }

    @Nested
    @DisplayName("findById Tests")
    class FindByIdTests {

        @Test
        @DisplayName("Should return ServiceDto when service exists")
        void testFindById_Found() {
            when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(service));

            Optional<ServiceDto> result = serviceService.findById(serviceId);

            assertTrue(result.isPresent());
            assertEquals(serviceId, result.get().id());
            assertEquals(service.resources().size(), result.get().resources().size());
            verify(serviceRepository, times(1)).findById(serviceId);
        }

        @Test
        @DisplayName("Should return empty Optional when service does not exist")
        void testFindById_NotFound() {
            when(serviceRepository.findById(serviceId)).thenReturn(Optional.empty());

            Optional<ServiceDto> result = serviceService.findById(serviceId);

            assertTrue(result.isEmpty());
            verify(serviceRepository, times(1)).findById(serviceId);
        }
    }

    @Nested
    @DisplayName("save Tests")
    class SaveTests {

        @Test
        @DisplayName("Should save service and return ServiceDto")
        void testSave() {
            when(serviceRepository.save(any(Service.class))).thenAnswer(invocation -> {
                Service arg = invocation.getArgument(0);
                return new Service(arg.id(), arg.resources(), 0L);
            });

            ServiceDto result = serviceService.save(serviceDto);

            assertNotNull(result);
            assertEquals(serviceDto.id(), result.id());
            assertEquals(serviceDto.resources().size(), result.resources().size());
            verify(serviceRepository, times(1)).save(any(Service.class));
        }
    }

    @Nested
    @DisplayName("update Tests")
    class UpdateTests {

        @Test
        @DisplayName("Should update service and return updated ServiceDto")
        void testUpdate_Success() throws NotFoundException {
            ServiceDto updatedDto = new ServiceDto(serviceId, List.of(new ResourceDto("newResourceId", List.of(new OwnerDto("newOwnerId", "New Owner", "0987654321", 2)))));

            when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(service));
            when(serviceRepository.findAndUpdateResourcesWithVersion(eq(serviceId), anyList(), eq(version)))
                    .thenReturn(new Service(serviceId, List.of(new Resource("newResourceId", List.of(new Owner("newOwnerId", "New Owner", "0987654321", 2)))), version + 1));

            ServiceDto result = serviceService.update(serviceId, updatedDto);

            assertNotNull(result);
            assertEquals(updatedDto.id(), result.id());
            assertEquals(updatedDto.resources().size(), result.resources().size());
            verify(serviceRepository, times(1)).findById(serviceId);
            verify(serviceRepository, times(1)).findAndUpdateResourcesWithVersion(eq(serviceId), anyList(), eq(version));
        }

        @Test
        @DisplayName("Should throw NotFoundException when service to update does not exist")
        void testUpdate_NotFound() {
            when(serviceRepository.findById(serviceId)).thenReturn(Optional.empty());

            NotFoundException exception = assertThrows(NotFoundException.class, () -> serviceService.update(serviceId, serviceDto));

            assertNotNull(exception);
            verify(serviceRepository, times(1)).findById(serviceId);
            verify(serviceRepository, never()).findAndUpdateResourcesWithVersion(anyString(), anyList(), anyLong());
        }

        @Test
        @DisplayName("Should throw OptimisticLockingFailureException on concurrent update")
        void testUpdate_OptimisticLocking() {
            when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(service));
            when(serviceRepository.findAndUpdateResourcesWithVersion(eq(serviceId), anyList(), eq(version))).thenReturn(null);

            OptimisticLockingFailureException exception = assertThrows(OptimisticLockingFailureException.class, () -> serviceService.update(serviceId, serviceDto));

            assertNotNull(exception);
            verify(serviceRepository, times(1)).findById(serviceId);
            verify(serviceRepository, times(1)).findAndUpdateResourcesWithVersion(eq(serviceId), anyList(), eq(version));
        }
    }

    @Nested
    @DisplayName("deleteById Tests")
    class DeleteByIdTests {

        @Test
        @DisplayName("Should delete service when service exists")
        void testDeleteById_Success() throws NotFoundException {
            when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(service));
            doNothing().when(serviceRepository).deleteById(serviceId);

            assertDoesNotThrow(() -> serviceService.deleteById(serviceId));

            verify(serviceRepository, times(1)).findById(serviceId);
            verify(serviceRepository, times(1)).deleteById(serviceId);
        }

        @Test
        @DisplayName("Should throw NotFoundException when service to delete does not exist")
        void testDeleteById_NotFound() {
            when(serviceRepository.findById(serviceId)).thenReturn(Optional.empty());

            NotFoundException exception = assertThrows(NotFoundException.class, () -> serviceService.deleteById(serviceId));

            assertNotNull(exception);
            verify(serviceRepository, times(1)).findById(serviceId);
            verify(serviceRepository, never()).deleteById(anyString());
        }
    }

    @Nested
    @DisplayName("findAllServiceSummaries Tests")
    class FindAllServiceSummariesTests {

        @Test
        @DisplayName("Should return list of ServiceSummaryDto")
        void testFindAllServiceSummaries() {
            List<ServiceSummaryDto> summaries = List.of(new ServiceSummaryDto(serviceId, 1));
            when(serviceRepository.findAllServiceSummaries()).thenReturn(summaries);

            List<ServiceSummaryDto> result = serviceService.findAllServiceSummaries();

            assertNotNull(result);
            assertEquals(summaries.size(), result.size());
            verify(serviceRepository, times(1)).findAllServiceSummaries();
        }

        @Test
        @DisplayName("Should return empty list when no services exist")
        void testFindAllServiceSummaries_Empty() {
            when(serviceRepository.findAllServiceSummaries()).thenReturn(Collections.emptyList());

            List<ServiceSummaryDto> result = serviceService.findAllServiceSummaries();

            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(serviceRepository, times(1)).findAllServiceSummaries();
        }
    }
}
