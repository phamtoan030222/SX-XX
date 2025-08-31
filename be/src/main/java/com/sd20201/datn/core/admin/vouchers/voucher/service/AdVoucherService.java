package com.sd20201.datn.core.admin.vouchers.voucher.service;


import com.sd20201.datn.core.admin.vouchers.voucher.model.request.AdVoucherRequest;
import com.sd20201.datn.core.admin.vouchers.voucher.model.request.VoucherValiDateRequest;
import com.sd20201.datn.core.common.base.ResponseObject;
import jakarta.validation.Valid;

public interface AdVoucherService {
    ResponseObject<?> getAllVoucher(AdVoucherRequest request);

    ResponseObject<?> createVoucher(@Valid VoucherValiDateRequest request);

    ResponseObject<?> updateVoucher(@Valid  VoucherValiDateRequest request);

    ResponseObject<?> deactivateVoucher(String id);

    ResponseObject<?> deleteVoucher(String id);
}
