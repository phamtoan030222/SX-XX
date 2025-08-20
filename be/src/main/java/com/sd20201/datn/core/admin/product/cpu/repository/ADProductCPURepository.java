package com.sd20201.datn.core.admin.product.cpu.repository;

import com.sd20201.datn.core.admin.product.cpu.model.request.ADProductCPURequest;
import com.sd20201.datn.core.admin.product.cpu.model.response.ADProductCPUResponse;
import com.sd20201.datn.repository.CPURepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ADProductCPURepository extends CPURepository {

    @Query(value = """
    SELECT c
    FROM CPU c
    where
        (
            :#{#request.q} is null or c.name like concat('%',:#{#request.q},'%')
            OR :#{#request.q} is null or c.code like concat('%',:#{#request.q},'%')
        )
    """, countQuery = """
    SELECT COUNT(1)
    FROM CPU c
    where
        (
            :#{#request.q} is null or c.name like concat('%',:#{#request.q},'%')
            OR :#{#request.q} is null or c.code like concat('%',:#{#request.q},'%')
        )""")
    List<ADProductCPUResponse> getCPUs(Pageable pageable, ADProductCPURequest request);

    Optional<ADProductCPUResponse> getCPUById(String id);
}
