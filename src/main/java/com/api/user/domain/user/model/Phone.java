package com.api.user.domain.user.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Phone {
    private int number;
    private int cityCode;
    private int countryCode;
}
