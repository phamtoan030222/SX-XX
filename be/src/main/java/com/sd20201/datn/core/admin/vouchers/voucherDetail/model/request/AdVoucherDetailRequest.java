package com.sd20201.datn.core.admin.vouchers.voucherDetail.model.request;

import com.sd20201.datn.core.common.base.PageableRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class AdVoucherDetailRequest extends PageableRequest {

    private String voucherDetailName;

    private Integer voucherDetailStatus;

    private String voucherDetailCode;

}
