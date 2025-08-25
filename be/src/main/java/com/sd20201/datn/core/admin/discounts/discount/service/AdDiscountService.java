package com.sd20201.datn.core.admin.discounts.discount.service;

import com.sd20201.datn.core.admin.discounts.discount.model.request.AdDiscountRequest;
import com.sd20201.datn.core.admin.discounts.discount.model.request.DiscountUpdateRequest;
import com.sd20201.datn.core.admin.discounts.discount.model.request.DiscountValidateRequest;

import com.sd20201.datn.core.common.base.ResponseObject;
import jakarta.validation.Valid;

public interface AdDiscountService {
    ResponseObject<?> getAllDiscounts(AdDiscountRequest request);

    ResponseObject<?> creatDiscount(@Valid DiscountValidateRequest request);

    ResponseObject<?> updateDiscount(@Valid DiscountUpdateRequest request);

    ResponseObject<?> deactivateDiscount(String id);

    ResponseObject<?> deleteDiscount(String id);
}
