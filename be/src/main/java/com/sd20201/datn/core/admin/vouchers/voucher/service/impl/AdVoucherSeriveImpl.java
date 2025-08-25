package com.sd20201.datn.core.admin.vouchers.voucher.service.impl;

import com.sd20201.datn.core.admin.vouchers.voucher.model.request.AdVoucherRequest;
import com.sd20201.datn.core.admin.vouchers.voucher.model.request.VoucherValiDateRequest;
import com.sd20201.datn.core.admin.vouchers.voucher.repository.AdVoucherRepository;
import com.sd20201.datn.core.admin.vouchers.voucher.service.AdVoucherService;
import com.sd20201.datn.core.common.base.PageableObject;
import com.sd20201.datn.core.common.base.ResponseObject;
import com.sd20201.datn.entity.Voucher;
import com.sd20201.datn.infrastructure.constant.EntityStatus;
import com.sd20201.datn.utils.Helper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor

public class AdVoucherSeriveImpl implements AdVoucherService {

    private final AdVoucherRepository adVoucherRepository;


    @Override
    public ResponseObject<?> getAllVoucher(AdVoucherRequest request) {
        Pageable pageable = Helper.createPageable(request, "createdDate");

        return new ResponseObject<>(
                PageableObject.of(adVoucherRepository.getAllVoucher(pageable, request.getVoucherName()
                                                                            , request.getVoucherStatus()
                                                                          )),
                HttpStatus.OK,
                "Lấy thành công danh sách Voucher "
        );
    }

    @Override
    public ResponseObject<?> createVoucher(VoucherValiDateRequest request) {
        List<Voucher> vouchersByName = adVoucherRepository.findAllVoucherByName(request.getVoucherName());
        if (!vouchersByName.isEmpty()) {
            return new ResponseObject<>(
                    null,
                    HttpStatus.BAD_REQUEST, "Têm voucher đã tồn tại",
                    false,
                    "VOUCHER_NAME_EXISTS"
            );
        }
            List<Voucher> vouchersByCode = adVoucherRepository.findAllVoucherByCode(request.getVoucherCode());
            if (!vouchersByCode.isEmpty()) {
                return new ResponseObject<>(
                        null,
                        HttpStatus.BAD_REQUEST,
                        "Mã voucher đã tồn tại",
                        false,
                        "VOUCHER_CODE_EXISTS"
                );
            }

        Long now = System.currentTimeMillis();
        if (request.getStartTime() != null && request.getEndTime() != null) {
            if (request.getStartTime() >= request.getEndTime()) {
                return new ResponseObject<>(
                        null,
                        HttpStatus.BAD_REQUEST,
                        "Thời gian bắt đầu phải nhỏ hơn thời gian kết thúc",
                        false,
                            "VOUCHER_TIME_INVALID"
                );
            }
            if (request.getStartTime() <= now) {
                return new ResponseObject<>(
                        null,
                        HttpStatus.BAD_REQUEST,
                        "Thời gian bắt đầu phải lớn hơn thời gian hiện tại",
                        false,
                        "VOUCHER_START_INVALID"
                );
            }
        }

            Voucher voucher = new Voucher();
            voucher.setTypeVoucher(request.getVoucherType());
            voucher.setName(request.getVoucherName());
            voucher.setCode(request.getVoucherCode());
            voucher.setDecreaseUnit(request.getDecreaseUnit());
            voucher.setIncreaseUnit(BigDecimal.valueOf(request.getIncreaseUnit()));
            voucher.setStartTime(request.getStartTime());
            voucher.setEndTime(request.getEndTime());
            voucher.setConditionOfUse(request.getConditionOfUse());
            voucher.setCreatedDate(System.currentTimeMillis());
            voucher.setStatus(EntityStatus.ACTIVE);
            adVoucherRepository.save(voucher);

            return new ResponseObject<>(
                    null,
                    HttpStatus.OK,
                    "Thêm voucher thành công",
                    true,
                    null
            );

    }

    @Override
    public ResponseObject<?> updateVoucher(VoucherValiDateRequest request) {
        Voucher voucher = adVoucherRepository.findById(request.getId())
                .orElse(null);

        if (voucher == null) {
            return new ResponseObject<>(
                    null,
                    HttpStatus.BAD_REQUEST,
                    "Voucher không tồn tại",
                    false,
                    "VOUCHER_NOT_FOUND"
            );
        }

        if (voucher.getStatus() != EntityStatus.ACTIVE) {
            return new ResponseObject<>(
                    null,
                    HttpStatus.BAD_REQUEST,
                    "Voucher không tồn tại",
                    false,
                    "VOUCHER_INACTIVE"
            );
        }

        if (!adVoucherRepository.findByNameAndNotId(request.getVoucherName(), request.getId()).isEmpty()) {
            return new ResponseObject<>(
                    null,
                    HttpStatus.BAD_REQUEST,
                    "Tên voucher đã tồn tại",
                    false,
                    "VOUCHER_NAME_EXISTS"
            );
        }


        if (!adVoucherRepository.findByCodeAndNotId(request.getVoucherCode(), request.getId()).isEmpty()) {
            return new ResponseObject<>(
                    null,
                    HttpStatus.BAD_REQUEST,
                    "Mã voucher đã tồn tại",
                    false,
                    "VOUCHER_CODE_EXISTS"
            );
        }

        if(voucher.getStartTime() != null && voucher.getStartTime() > voucher.getEndTime()){
            return new ResponseObject<>(
                    null,
                    HttpStatus.BAD_REQUEST,
                    "Thời gian bắt đầu phải nhỏ hơn thời gian kết thúc",
                    false,
                    "VOUCHER_EXPIRED"
            );
        }

        long now = System.currentTimeMillis();

        if(voucher.getStartTime() != null && voucher.getStartTime() < now){
            return new ResponseObject<>(
                    null,
                    HttpStatus.BAD_REQUEST,
                    "Thời gian bắt đầu phải lớn hơn thời gian hiện tại",
                    false,
                    "VOUCHER_EXPIRED"
            );
        }

        if (voucher.getEndTime() != null && voucher.getEndTime() <= now) {
            return new ResponseObject<>(
                    null,
                    HttpStatus.BAD_REQUEST,
                    "Voucher đã kết thúc, không thể cập nhật",
                    false,
                    "VOUCHER_EXPIRED"
            );
        }

        if (voucher.getStartTime() != null && voucher.getStartTime() <= now && voucher.getEndTime() != null && voucher.getEndTime() > now) {
            return new ResponseObject<>(
                    null,
                    HttpStatus.BAD_REQUEST,
                    "VOUCHER đang diễn ra, không thể cập nhật",
                    false,
                    "VOUCHER_RUNNING"
            );
        }

        if(voucher.getStartTime() != null && voucher.getStartTime() > voucher.getEndTime()){
            return new ResponseObject<>(
                    null,
                    HttpStatus.BAD_REQUEST,
                    "Thời gian bắt đầu phải nhỏ hơn thời gian kết thúc",
                    false,
                    "VOUCHER_EXPIRED"
            );
        }


        voucher.setTypeVoucher(request.getVoucherType());
        voucher.setName(request.getVoucherName());
        voucher.setCode(request.getVoucherCode());
        voucher.setDecreaseUnit(request.getDecreaseUnit());
        voucher.setIncreaseUnit(BigDecimal.valueOf(request.getIncreaseUnit()));
        voucher.setStartTime(request.getStartTime());
        voucher.setEndTime(request.getEndTime());
        voucher.setConditionOfUse(request.getConditionOfUse());
        voucher.setLastModifiedDate(System.currentTimeMillis());

        adVoucherRepository.save(voucher);

        return new ResponseObject<>(
                voucher,
                HttpStatus.OK,
                "Cập nhật voucher thành công",
                true,
                null
        );
    }

    @Override
    public ResponseObject<?> deactivateVoucher(String id) {
        Voucher voucher = adVoucherRepository.findById(id).orElse(null);
        if (voucher == null) {
            return new ResponseObject<>(
                    null,
                    HttpStatus.NOT_FOUND,
                    "Voucher không tồn tại",
                    false,
                    "VOUCHER_NOT_FOUND"
            );
        }

        if (EntityStatus.INACTIVE.equals(voucher.getStatus())) {
            return new ResponseObject<>(
                    null,
                    HttpStatus.BAD_REQUEST,
                    "Voucher không tồn tại",
                    false,
                    "VOUCHER_ALREADY_INACTIVE"
            );
        }

        long now = System.currentTimeMillis();

        if(voucher.getStartTime() != null && voucher.getEndTime() < now ){
            return new ResponseObject<>(
                    null,
                    HttpStatus.BAD_REQUEST,
                    "Voucher kết thúc",
                    false,
                    "VOUCHER_EXPIRED"
            );
        }

        if(voucher.getStartTime() != null && voucher.getStartTime() > now ){
            return new ResponseObject<>(
                    null,
                    HttpStatus.BAD_REQUEST,
                    "Không thể kết thúc",
                    false,
                    "VOUCHER_EXPIRED"
            );
        }

        voucher.setStatus(EntityStatus.INACTIVE);
        voucher.setLastModifiedDate(System.currentTimeMillis());
        adVoucherRepository.save(voucher);

        return new ResponseObject<>(
                voucher,
                HttpStatus.OK,
                "Voucher đã được kết thúc sớm",
                true,
                null
        );
    }

    @Override
    public ResponseObject<?> deleteVoucher(String id) {
        Voucher voucher = adVoucherRepository.findById(id)
                .orElse(null);

        if (voucher == null) {
            return new ResponseObject<>(
                    null,
                    HttpStatus.BAD_REQUEST,
                    "Voucher không tồn tại",
                    false,
                    "VOUCHER_NOT_FOUND"
            );
        }

        long now = System.currentTimeMillis();


        if (voucher.getStartTime() != null && voucher.getStartTime() <= now) {
            return new ResponseObject<>(
                    null,
                    HttpStatus.BAD_REQUEST,
                    "Voucher đã bắt đầu hoặc đã kết thúc, không thể xóa",
                    false,
                    "VOUCHER_STARTED"
            );
        }

        adVoucherRepository.delete(voucher);

        return new ResponseObject<>(
                null,
                HttpStatus.OK,
                "Xóa voucher thành công",
                true,
                null
        );
    }


}
