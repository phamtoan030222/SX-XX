package com.sd20201.datn.core.admin.discounts.discount.service.impl;

import com.sd20201.datn.core.admin.discounts.discount.model.request.AdDiscountRequest;
import com.sd20201.datn.core.admin.discounts.discount.model.request.DiscountUpdateRequest;
import com.sd20201.datn.core.admin.discounts.discount.model.request.DiscountValidateRequest;
import com.sd20201.datn.core.admin.discounts.discount.repository.AdDiscountRepossitory;
import com.sd20201.datn.core.admin.discounts.discount.service.AdDiscountService;
import com.sd20201.datn.core.common.base.PageableObject;
import com.sd20201.datn.core.common.base.ResponseObject;
import com.sd20201.datn.entity.Discount;
import com.sd20201.datn.infrastructure.constant.EntityStatus;
import com.sd20201.datn.utils.Helper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdDiscountServiceImpl implements AdDiscountService {

    private final AdDiscountRepossitory adDiscountRepossitory;

    @Override
    public ResponseObject<?> getAllDiscounts(AdDiscountRequest request) {
        Pageable pageable = Helper.createPageable(request, "createdDate");

        return new ResponseObject<>(
                PageableObject.of(adDiscountRepossitory.getAllDiscount(pageable, request.getDiscountName()
                        , request.getDiscountStatus()
                )),
                HttpStatus.OK,
                "Lấy thành công danh sách Đợt giảm giá "
        );

    }

    @Override
    public ResponseObject<?> creatDiscount(DiscountValidateRequest request) {
        List<Discount> discountsByName = adDiscountRepossitory.findAllDiscountByName(request.getDiscountName());
        if (!discountsByName.isEmpty()) {
            return new ResponseObject<>(
                    null,
                    HttpStatus.BAD_REQUEST, "Têm Đợt giảm giá đã tồn tại",
                    false,
                    "DISCOUNT_NAME_EXISTS"
            );
        }
        List<Discount> discountsCode = adDiscountRepossitory.findAlDiscountByCode(request.getDiscountCode());
        if (!discountsCode.isEmpty()) {
            return new ResponseObject<>(
                    null,
                    HttpStatus.BAD_REQUEST,
                    "Mã đợt giảm giá đã tồn tại",
                    false,
                    "DISCOUNT_CODE_EXISTS"
            );
        }

        Long now = System.currentTimeMillis();
        if (request.getStartDate() != null && request.getEndDate() != null) {
            if (request.getStartDate() >= request.getEndDate()) {
                return new ResponseObject<>(
                        null,
                        HttpStatus.BAD_REQUEST,
                        "Thời gian bắt đầu phải nhỏ hơn thời gian kết thúc",
                        false,
                        "DISCOUNT_TIME_INVALID"
                );
            }
//            if (request.getStartDate() <= now) {
//                return new ResponseObject<>(
//                        null,
//                        HttpStatus.BAD_REQUEST,
//                        "Thời gian bắt đầu phải lớn hơn thời gian hiện tại",
//                        false,
//                        "DISCOUNT_START_INVALID"
//                );
//            }
        }

        Discount discount = new Discount();
        discount.setName(request.getDiscountName());
        discount.setCode(request.getDiscountCode());
        discount.setStartDate(request.getStartDate());
        discount.setEndDate(request.getEndDate());
        discount.setDescription(request.getDescription());
        discount.setPercentage(request.getPercentage());
        discount.setCreatedDate(System.currentTimeMillis());
        discount.setStatus(EntityStatus.ACTIVE);
        adDiscountRepossitory.save(discount);

        return new ResponseObject<>(
                null,
                HttpStatus.OK,
                "Thêm Đợt giảm giá thành công thành công",
                true,
                null
        );
    }

    @Override
    public ResponseObject<?> updateDiscount(DiscountUpdateRequest request) {
        Discount discount = adDiscountRepossitory.findById(request.getId())
                .orElse(null);

        if (discount == null) {
            return new ResponseObject<>(
                    null,
                    HttpStatus.BAD_REQUEST,
                    "Đởt giảm giá không tồn tại",
                    false,
                    "DISCOUNT_NOT_FOUND"
            );
        }

        if (discount.getStatus() != EntityStatus.ACTIVE) {
            return new ResponseObject<>(
                    null,
                    HttpStatus.BAD_REQUEST,
                    "Đợt giảm giá không tồn tại",
                    false,
                    "DISCOUNT_INACTIVE"
            );
        }

        if (!adDiscountRepossitory.findByNameAndNotId(request.getDiscountName(), request.getId()).isEmpty()) {
            return new ResponseObject<>(
                    null,
                    HttpStatus.BAD_REQUEST,
                    "Tên đợt giảm giá đã tồn tại",
                    false,
                    "DISCOUNT_NAME_EXISTS"
            );
        }


        if (!adDiscountRepossitory.findByCodeAndNotId(request.getDiscountCode(), request.getId()).isEmpty()) {
            return new ResponseObject<>(
                    null,
                    HttpStatus.BAD_REQUEST,
                    "Mã đợt giảm giá đã tồn tại",
                    false,
                    "DISCOUNT_CODE_EXISTS"
            );
        }

        long now = System.currentTimeMillis();


        if (discount.getEndDate() != null && discount.getEndDate() <= now) {
            return new ResponseObject<>(
                    null,
                    HttpStatus.BAD_REQUEST,
                    "Đợt giảm giá đã kết thúc, không thể cập nhật",
                    false,
                    "DISCOUNT_EXPIRED"
            );
        }

        if (discount.getStartDate() != null && discount.getStartDate() <= now) {
            return new ResponseObject<>(
                    null,
                    HttpStatus.BAD_REQUEST,
                    "Đợt giảm giá đang diễn ra, không thể cập nhật",
                    false,
                    "DISCOUNT_RUNNING"
            );
        }



        discount.setName(request.getDiscountName());
        discount.setCode(request.getDiscountCode());
        discount.setStartDate(request.getStartDate());
        discount.setEndDate(request.getEndDate());
        discount.setDescription(request.getDescription());
        discount.setPercentage(request.getPercentage());
        discount.setCreatedDate(System.currentTimeMillis());
        discount.setStatus(EntityStatus.ACTIVE);
        adDiscountRepossitory.save(discount);

        return new ResponseObject<>(
                null,
                HttpStatus.OK,
                "Update Đợt giảm giá thành công thành công",
                true,
                null
        );
    }

    @Override
    public ResponseObject<?> deactivateDiscount(String id) {
        Discount discount = adDiscountRepossitory.findById(id).orElse(null);
        if (discount == null) {
            return new ResponseObject<>(
                    null,
                    HttpStatus.NOT_FOUND,
                    "Đợt giảm giá không tồn tại",
                    false,
                    "DISCOUNT_NOT_FOUND"
            );
        }

        if (EntityStatus.INACTIVE.equals(discount.getStatus())) {
            return new ResponseObject<>(
                    null,
                    HttpStatus.BAD_REQUEST,
                    "Đợt giảm giá không tồn tại",
                    false,
                    "DISCOUNT_ALREADY_INACTIVE"
            );
        }

        long now = System.currentTimeMillis();


        if (discount.getEndDate() != null && discount.getEndDate() <= now) {
            return new ResponseObject<>(
                    null,
                    HttpStatus.BAD_REQUEST,
                    "Đợt giảm giá đã kết thúc, không thể làm gì!!!",
                    false,
                    "DISCOUNT_EXPIRED"
            );
        }

        if (discount.getStartDate() != null && discount.getStartDate() > now) {
            return new ResponseObject<>(
                    null,
                    HttpStatus.BAD_REQUEST,
                    "Đợt giảm giá chưa diễn ra, không thể kết thúc",
                    false,
                    "DISCOUNT_RUNNING"
            );
        }

        discount.setStatus(EntityStatus.INACTIVE);
        discount.setLastModifiedDate(System.currentTimeMillis());
        adDiscountRepossitory.save(discount);

        return new ResponseObject<>(
                discount,
                HttpStatus.OK,
                "Đợt giảm giá đã được kết thúc sớm",
                true,
                null
        );
    }

    @Override
    public ResponseObject<?> deleteDiscount(String id) {
        Discount discount = adDiscountRepossitory.findById(id)
                .orElse(null);

        if (discount == null) {
            return new ResponseObject<>(
                    null,
                    HttpStatus.BAD_REQUEST,
                    "Đợt giảm giá không tồn tại",
                    false,
                    "DISCOUNT_NOT_FOUND"
            );
        }

        long now = System.currentTimeMillis();
        if (discount.getStartDate() != null && discount.getStartDate() <= now) {
            return new ResponseObject<>(
                    null,
                    HttpStatus.BAD_REQUEST,
                    "Đợt giảm giá đã bắt đầu hoặc đã kết thúc, không thể xóa",
                    false,
                    "DISCOUNT_STARTED"
            );
        }

        adDiscountRepossitory.delete(discount);

        return new ResponseObject<>(
                null,
                HttpStatus.OK,
                "Xóa đợt giảm giá thành công",
                true,
                null
        );
    }
}
