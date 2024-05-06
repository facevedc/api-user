package com.api.user.api.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Phone {
    private Integer number;
    private Integer city_code;
    private Integer country_code;
}
