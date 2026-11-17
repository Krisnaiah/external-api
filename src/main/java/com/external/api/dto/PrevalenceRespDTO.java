package com.external.api.dto;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrevalenceRespDTO {
    private Long id;
    private String country;
    private String code;
    private int year;
    private double prevalence;
    private Instant createdAt;
    private Instant updatedAt;
}
