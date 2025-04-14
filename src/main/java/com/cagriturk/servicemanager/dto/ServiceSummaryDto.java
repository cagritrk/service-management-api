package com.cagriturk.servicemanager.dto;

/**
 * DTO representing a summary of a Service, containing its ID and resource count.
 */
public record ServiceSummaryDto(
    String id,
    int resourceCount
) {}