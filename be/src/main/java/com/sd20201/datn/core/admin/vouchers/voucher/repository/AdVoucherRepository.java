package com.sd20201.datn.core.admin.vouchers.voucher.repository;

import com.sd20201.datn.core.admin.vouchers.voucher.model.response.AdVoucherResponse;
import com.sd20201.datn.entity.Voucher;
import com.sd20201.datn.repository.VoucherRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface AdVoucherRepository extends VoucherRepository {

    @Query(
            value = """
           SELECT c.id AS id,
                  c.name AS voucherName,
                  c.code AS voucherCode,
                  c.createdDate AS createdDate,
                  c.startTime AS startTime,
                  c.endTime AS endTime,
                  c.conditionOfUse AS conditionOfUse,
                  c.decreaseUnit AS voucherDecreaseUnit,
                  c.increaseUnit AS increaseUnit,
                  c.typeVoucher AS typeVoucher,
                  c.status AS voucherStatus
           FROM Voucher c 
           WHERE (:voucherName IS NULL OR c.name LIKE CONCAT('%', :voucherName, '%'))
                 AND (:voucherStatus IS NULL OR c.status = :voucherStatus)
                 AND c.status = 0
           ORDER BY c.createdDate DESC
    """,
            countQuery = """
           SELECT COUNT(c.id)
           FROM Voucher c 
           WHERE (:voucherName IS NULL OR c.name LIKE CONCAT('%', :voucherName, '%'))
                 AND (:voucherStatus IS NULL OR c.status = :voucherStatus)
    """
    )
    Page<AdVoucherResponse> getAllVoucher(Pageable pageable,
            @Param("voucherName") String voucherName,
            @Param("voucherStatus") Integer voucherStatus);

    List<Voucher> findAllVoucherByCode(String code);
    List<Voucher> findAllVoucherByName(String Name);

    @Query("SELECT v FROM Voucher v WHERE v.name = :voucherName AND v.id <> :id")
    List<Voucher> findByNameAndNotId(@Param("voucherName") String voucherName,
                                     @Param("id") String id);

    @Query("SELECT v FROM Voucher v WHERE v.code = :voucherCode AND v.id <> :id")
    List<Voucher> findByCodeAndNotId(@Param("voucherCode") String voucherCode,
                                     @Param("id") String id);

}
