package com.sd20201.datn.core.admin.vouchers.voucher.model.request;

import com.sd20201.datn.core.common.base.PageableRequest;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter

public class AdVoucherRequest extends PageableRequest {

    private String voucherName;

    private Integer voucherStatus;

}
