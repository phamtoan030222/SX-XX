package com.sd20201.datn.core.admin.vouchers.voucherDetail.model.response;


import java.math.BigDecimal;

public interface AdVoucherDetailResponse {
    String getId();
    String getVoucherDetailName();
    String getVoucherDetailCode();
    String getVoucherDetailDescription();
    Boolean getUsageStatus();
    Boolean getVoucherDetailStatus();
    String getCustomerName();
    BigDecimal getConditionOfUse();
    Integer getVoucherDecreaseUnit();
    Integer getIncreaseUnit();
    Long getCreatedDate();
}
