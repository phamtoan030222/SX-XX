package com.sd20201.datn.core.admin.vouchers.voucher.model.response;

import java.math.BigDecimal;

public interface AdVoucherResponse {
    String getId();
    String getVoucherName();
    String getVoucherCode();
    Long getCreatedDate();
    Long getStartTime();
    Long getEndTime();
    BigDecimal getConditionOfUse();
    Integer getVoucherDecreaseUnit();
    Integer getIncreaseUnit();
    Integer getTypeVoucher();
    Integer getVoucherStatus();

}
