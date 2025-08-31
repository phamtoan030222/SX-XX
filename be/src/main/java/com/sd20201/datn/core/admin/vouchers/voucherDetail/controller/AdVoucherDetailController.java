package com.sd20201.datn.core.admin.vouchers.voucherDetail.controller;

import com.sd20201.datn.core.admin.vouchers.voucherDetail.model.request.AdVoucherDetailRequest;
import com.sd20201.datn.core.admin.vouchers.voucherDetail.model.request.AdVoucherDetailValidateRequest;
import com.sd20201.datn.core.admin.vouchers.voucherDetail.service.AdVoucherDetailService;
import com.sd20201.datn.infrastructure.constant.MappingConstants;
import com.sd20201.datn.utils.Helper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(MappingConstants.API_ADMIN_PREFIX_VOUCHER_DETAIL)
@RequiredArgsConstructor
public class AdVoucherDetailController {
    private final AdVoucherDetailService adVoucherDetailService;
    @GetMapping()
    public ResponseEntity<?> getALLVoucher(@ModelAttribute AdVoucherDetailRequest request){
        return Helper.createResponseEntity(adVoucherDetailService.getAllVoucherDetail(request));
    }
    @PostMapping("/addVoucherDetail")
    public ResponseEntity<?> addVoucherDetail(@RequestBody @Valid AdVoucherDetailValidateRequest request){
        return Helper.createResponseEntity(adVoucherDetailService.createVoucherDetail(request));
    }
}
