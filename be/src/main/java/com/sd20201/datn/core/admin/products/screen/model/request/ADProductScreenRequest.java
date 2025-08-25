package com.sd20201.datn.core.admin.products.screen.model.request;

import com.sd20201.datn.core.common.base.PageableRequest;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ADProductScreenRequest extends PageableRequest {

    private Float physicalSize;

    private String idScreenResolution;

    private String panelType;

    private String technology;


}
