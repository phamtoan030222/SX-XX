package com.sd20201.datn.core.admin.vouchers.voucherDetail.service.impl;

import com.sd20201.datn.core.admin.vouchers.voucher.repository.AdVoucherRepository;
import com.sd20201.datn.core.admin.vouchers.voucherDetail.model.request.AdVoucherDetailRequest;
import com.sd20201.datn.core.admin.vouchers.voucherDetail.model.request.AdVoucherDetailValidateRequest;
import com.sd20201.datn.core.admin.vouchers.voucherDetail.repository.AdVoucherDetailRepository;
import com.sd20201.datn.core.admin.vouchers.voucherDetail.service.AdVoucherDetailService;
import com.sd20201.datn.core.common.base.PageableObject;
import com.sd20201.datn.core.common.base.ResponseObject;
import com.sd20201.datn.entity.Customer;
import com.sd20201.datn.entity.Voucher;
import com.sd20201.datn.entity.VoucherDetail;
import com.sd20201.datn.infrastructure.constant.EntityStatus;
import com.sd20201.datn.repository.CustomerRepository;
import com.sd20201.datn.utils.Helper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdVoucherDetailServiceImpl implements AdVoucherDetailService {

    private final AdVoucherDetailRepository adVoucherDetailRepository;
    @Autowired
    private final AdVoucherRepository voucherRepository;

    @Autowired
    private final CustomerRepository customerRepository;

    @Override
    public ResponseObject<?> getAllVoucherDetail(AdVoucherDetailRequest request) {
        Pageable pageable = Helper.createPageable(request, "createdDate");
        return new ResponseObject<>(
                PageableObject.of(adVoucherDetailRepository.getAllVoucher(pageable, request.getVoucherDetailName()
                        , request.getVoucherDetailStatus()
                )),
                HttpStatus.OK,
                "Lấy thành công danh sách Voucher Khách Hàng "
        );
    }

    @Override
    public ResponseObject<?> createVoucherDetail(@Valid AdVoucherDetailValidateRequest request) {
        VoucherDetail voucherDetail = new VoucherDetail();
        voucherDetail.setName(request.getVoucherDetailName());
        voucherDetail.setCode(request.getVoucherDetailCode());

        // Lấy Voucher từ idVoucher
        Voucher voucher = voucherRepository.findById(request.getIdVoucher())
                .orElseThrow(() -> new RuntimeException("Voucher không tồn tại"));
        voucherDetail.setVoucher(voucher);

        // Lấy Customer từ idCustomer
        Customer customer = customerRepository.findById(request.getIdCustomer())
                .orElseThrow(() -> new RuntimeException("Customer không tồn tại"));
        voucherDetail.setCustomer(customer);

        voucherDetail.setCreatedDate(System.currentTimeMillis());
        voucherDetail.setStatus(EntityStatus.ACTIVE);

        adVoucherDetailRepository.save(voucherDetail);

        return new ResponseObject<>(
                null,
                HttpStatus.OK,
                "Thêm Voucher Khách Hàng thành công",
                true,
                null
        );
    }

    @Override
    public ResponseObject<?> updateVoucherDetail(AdVoucherDetailValidateRequest request) {
        return null;
    }

    @Override
    public ResponseObject<?> deactivateVoucherDetail(String id) {
        return null;
    }

    @Override
    public ResponseObject<?> deleteVoucherDetail(String id) {
        return null;
    }
}
