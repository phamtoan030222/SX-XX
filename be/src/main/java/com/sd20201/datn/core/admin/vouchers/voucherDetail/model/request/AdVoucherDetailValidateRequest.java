package com.sd20201.datn.core.admin.vouchers.voucherDetail.model.request;

import com.sd20201.datn.core.common.base.PageableRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdVoucherDetailValidateRequest {

//    @NotNull(message = "Id không được để trống khi update")
//    private String id;

    @NotNull(message = "Tên không được để trống ")
    private String voucherDetailName;

    @NotNull(message = "Mã không được để trống ")
    private String voucherDetailCode;

    @NotNull(message = "Khách hàng không được để trống ")
    private String idCustomer;

    @NotNull(message = "Voucher không được để trống ")
    private String idVoucher;

}
