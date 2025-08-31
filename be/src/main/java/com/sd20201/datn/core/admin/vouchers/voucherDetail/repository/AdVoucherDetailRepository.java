package com.sd20201.datn.core.admin.vouchers.voucherDetail.repository;

import com.sd20201.datn.core.admin.vouchers.voucher.model.response.AdVoucherResponse;
import com.sd20201.datn.core.admin.vouchers.voucherDetail.model.response.AdVoucherDetailResponse;
import com.sd20201.datn.repository.VoucherDetailRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AdVoucherDetailRepository extends VoucherDetailRepository {
    @Query(
            value = """
           SELECT c.id AS id,
                         c.name AS voucherDetailName,
                         c.code AS voucherDetailCode,
                         c.description AS voucherDetailDescription,
                         c.usageStatus AS usageStatus,
                         c.status AS voucherDetailStatus,
                         cust.name AS customerName,
                         v.conditionOfUse AS conditionOfUse,
                         v.decreaseUnit AS voucherDecreaseUnit,
                         v.increaseUnit AS increaseUnit,
                         c.createdDate AS createdDate
                  FROM VoucherDetail c
                  JOIN c.customer cust
                  JOIN c.voucher v      
           WHERE (:voucherDetailName IS NULL OR c.name LIKE CONCAT('%', :voucherDetailName, '%'))
                 AND (:voucherDetailStatus IS NULL OR c.status = :voucherDetailStatus)
                 AND c.status = 0
           ORDER BY c.createdDate DESC
    """,
            countQuery = """
           SELECT COUNT(c.id)
           FROM VoucherDetail c 
           WHERE (:voucherDetailName IS NULL OR c.name LIKE CONCAT('%', :voucherDetailName, '%'))
                 AND (:voucherDetailStatus IS NULL OR c.status = :voucherDetailStatus)
    """
    )
    Page<AdVoucherDetailResponse> getAllVoucher(Pageable pageable,
                                                @Param("voucherDetailName") String voucherDetailName,
                                                @Param("voucherDetailStatus") Integer voucherDetailStatus);
}
