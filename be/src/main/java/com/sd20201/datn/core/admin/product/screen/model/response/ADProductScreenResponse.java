package com.sd20201.datn.core.admin.product.screen.model.response;

import com.sd20201.datn.core.common.base.IsIdentify;

public interface ADProductScreenResponse extends IsIdentify {

    String getCode();

    String getName();

    Float getPhysicalSize();

    String getIdScreenResolution();

    String getMaterial();

    String getTechnology();


}
