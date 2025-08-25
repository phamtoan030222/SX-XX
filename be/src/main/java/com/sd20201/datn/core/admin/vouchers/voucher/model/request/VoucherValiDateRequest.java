package com.sd20201.datn.core.admin.vouchers.voucher.model.request;
import com.sd20201.datn.infrastructure.constant.TypeVoucher;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class VoucherValiDateRequest {

    @NotNull(message = "Id không được để trống khi update")
    private String id;

    @NotBlank(message = "Tên voucher không được để trống")
    private String voucherName;

    @NotBlank(message = "Mã voucher không được để trống")
    private String voucherCode;

    @NotNull(message = "Loại voucher không được để trống")
    private TypeVoucher voucherType;

    @NotNull(message = "Đơn vị giảm không được để trống")
    private Integer decreaseUnit;

    @NotNull(message = "Đơn vị tăng không được để trống")
    private Integer increaseUnit;

    @NotNull(message = "Thời gian bắt đầu không được để trống")
    private Long startTime;

    @NotNull(message = "Thời gian kết thúc không được để trống")
    private Long endTime;

    @Positive(message = "Điều kiện sử dụng phải lớn hơn 0")
    private BigDecimal conditionOfUse;


}
