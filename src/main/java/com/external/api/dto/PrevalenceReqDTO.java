package com.external.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrevalenceReqDTO {
    private String country;
    private String code;
    private int year;
    private double prevalence;
}