package com.sd20201.datn.core.admin.discounts.discount.controller;

import com.sd20201.datn.core.admin.discounts.discount.model.request.AdDiscountRequest;
import com.sd20201.datn.core.admin.discounts.discount.model.request.DiscountUpdateRequest;
import com.sd20201.datn.core.admin.discounts.discount.model.request.DiscountValidateRequest;
import com.sd20201.datn.core.admin.discounts.discount.service.AdDiscountService;
import com.sd20201.datn.infrastructure.constant.MappingConstants;
import com.sd20201.datn.utils.Helper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(MappingConstants.API_ADMIN_PREFIX_DISCOUNT_DISCOUNT)
@RequiredArgsConstructor
public class AdDiscountController {
    private final AdDiscountService adDiscountService;
    @GetMapping()
    public ResponseEntity<?> getALLDiscount(@ModelAttribute AdDiscountRequest request){
        return Helper.createResponseEntity(adDiscountService.getAllDiscounts(request));
    }

    @PostMapping("/addDiscount")
    public ResponseEntity<?> addDiscount(@Valid @RequestBody DiscountValidateRequest request){
        return Helper.createResponseEntity(adDiscountService.creatDiscount(request));
    }


    @PutMapping("/updateDiscount")
    public ResponseEntity<?> updateDiscount(@Valid @RequestBody DiscountUpdateRequest request){
        return Helper.createResponseEntity(adDiscountService.updateDiscount(request));
    }

    @PutMapping("/deactivate/{id}")
    public ResponseEntity<?> deactivate(@PathVariable String id) {
        return Helper.createResponseEntity(adDiscountService.deactivateDiscount(id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        return Helper.createResponseEntity(adDiscountService.deleteDiscount(id));
    }

}
