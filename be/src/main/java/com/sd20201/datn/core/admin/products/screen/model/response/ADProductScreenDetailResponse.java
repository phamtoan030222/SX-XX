package com.sd20201.datn.core.admin.products.screen.model.response;

import com.sd20201.datn.core.common.base.IsIdentify;

public interface ADProductScreenDetailResponse extends IsIdentify {

    String getCode();

    String getName();

    Float getPhysicalSize();

    String getIdScreenResolution();

    String getPanelType();

    String getTechnology();

}
