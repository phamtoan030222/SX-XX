package com.sd20201.datn.core.admin.vouchers.voucher.controller;

import com.sd20201.datn.core.admin.vouchers.voucher.model.request.AdVoucherRequest;
import com.sd20201.datn.core.admin.vouchers.voucher.model.request.VoucherValiDateRequest;
import com.sd20201.datn.core.admin.vouchers.voucher.service.AdVoucherService;

import com.sd20201.datn.infrastructure.constant.MappingConstants;
import com.sd20201.datn.utils.Helper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(MappingConstants.API_ADMIN_PREFIX_VOUCHER_VOUCHER)
@RequiredArgsConstructor
public class AdVoucherController {

    private final AdVoucherService adVoucherService;

    @GetMapping()
    public ResponseEntity<?> getALLVoucher(@ModelAttribute AdVoucherRequest request){
        return Helper.createResponseEntity(adVoucherService.getAllVoucher(request));
    }

    @PostMapping("/addVoucher")
    public ResponseEntity<?> addVoucher(@Valid @RequestBody VoucherValiDateRequest request){
        return Helper.createResponseEntity(adVoucherService.createVoucher(request));
    }

    @PutMapping("/updateVoucher")
    public ResponseEntity<?> updateVoucher(@Valid @RequestBody VoucherValiDateRequest request){
        return Helper.createResponseEntity(adVoucherService.updateVoucher(request));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<?> deactivateVoucher(@PathVariable String id){
        return Helper.createResponseEntity(adVoucherService.deactivateVoucher(id));
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deleteVoucher(@PathVariable String id) {
        return Helper.createResponseEntity(adVoucherService.deleteVoucher(id));
    }



}
