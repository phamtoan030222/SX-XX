package com.sd20201.datn.core.admin.vouchers.voucherDetail.service;

import com.sd20201.datn.core.admin.vouchers.voucherDetail.model.request.AdVoucherDetailRequest;
import com.sd20201.datn.core.admin.vouchers.voucherDetail.model.request.AdVoucherDetailValidateRequest;
import com.sd20201.datn.core.common.base.ResponseObject;
import jakarta.validation.Valid;

public interface AdVoucherDetailService {
    ResponseObject<?> getAllVoucherDetail(AdVoucherDetailRequest request);

    ResponseObject<?> createVoucherDetail(@Valid AdVoucherDetailValidateRequest request);

    ResponseObject<?> updateVoucherDetail(@Valid  AdVoucherDetailValidateRequest request);

    ResponseObject<?> deactivateVoucherDetail(String id);

    ResponseObject<?> deleteVoucherDetail(String id);
}
