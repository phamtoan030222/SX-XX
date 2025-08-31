package com.sd20201.datn.repository;

import com.sd20201.datn.entity.ProductDetailDiscount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountDetailRepository extends JpaRepository<ProductDetailDiscount,String> {
}
