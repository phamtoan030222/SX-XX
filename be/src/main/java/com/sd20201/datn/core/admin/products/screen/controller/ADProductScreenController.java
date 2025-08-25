package com.sd20201.datn.core.admin.products.screen.controller;

import com.sd20201.datn.core.admin.products.screen.model.request.ADProductScreenCreateUpdateRequest;
import com.sd20201.datn.core.admin.products.screen.model.request.ADProductScreenRequest;
import com.sd20201.datn.core.admin.products.screen.service.ADProductScreenService;
import com.sd20201.datn.infrastructure.constant.MappingConstants;
import com.sd20201.datn.utils.Helper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(MappingConstants.API_ADMIN_PREFIX_PRODUCTS_SCREEN)
@RequiredArgsConstructor
public class ADProductScreenController {

    private final ADProductScreenService screenService;

    @GetMapping
    ResponseEntity<?> getScreens(ADProductScreenRequest request) {
        return Helper.createResponseEntity(screenService.getScreens(request));
    }

    @GetMapping("/resolutions")
    ResponseEntity<?> getResolutionScreens() {
        return Helper.createResponseEntity(screenService.getResolutionScreens());
    }

    @GetMapping("/{id}")
    ResponseEntity<?> getScreen(@PathVariable String id) {
        return Helper.createResponseEntity(screenService.getDetail(id));
    }

    @GetMapping("/change-status/{id}")
    ResponseEntity<?> changeStatus(@PathVariable String id) {
        return Helper.createResponseEntity(screenService.changeStatus(id));
    }

    @PostMapping
    ResponseEntity<?> modifyScreen(@RequestBody ADProductScreenCreateUpdateRequest request) {
        return Helper.createResponseEntity(screenService.modify(request));
    }

}
