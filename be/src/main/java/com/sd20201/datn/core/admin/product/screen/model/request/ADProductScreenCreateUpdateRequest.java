package com.sd20201.datn.core.admin.product.screen.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ADProductScreenCreateUpdateRequest {

    private String id;

    private String code;

    private String name;

    private Float physicalSize;

    private String idScreenResolution;

    private String material;

    private String technology;

}
