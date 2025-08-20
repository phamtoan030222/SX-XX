package com.sd20201.datn.core.admin.product.screen.repository;

import com.sd20201.datn.core.admin.product.screen.model.request.ADProductScreenRequest;
import com.sd20201.datn.core.admin.product.screen.model.response.ADProductScreenResponse;
import com.sd20201.datn.repository.ScreenRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ADProductScreenRepository extends ScreenRepository {

    @Query(value = """
    SELECT 
            s.code
            , s.name
            , s.physicalSize
            , s.resolution.id as idScreenResolution
            , s.material
            , s.technology
    FROM Screen s
    where
        (
            :#{#request.q} is null or s.name like concat('%',:#{#request.q},'%')
            OR :#{#request.q} is null or s.code like concat('%',:#{#request.q},'%')
        )
    """, countQuery = """
    SELECT COUNT(1)
    FROM Screen s
    where
        (
            :#{#request.q} is null or s.name like concat('%',:#{#request.q},'%')
            OR :#{#request.q} is null or s.code like concat('%',:#{#request.q},'%')
        )""")
    List<ADProductScreenResponse> getScreens(Pageable pageable, ADProductScreenRequest request);

    @Query(value = """
    SELECT
            s.code
            , s.name
            , s.physicalSize
            , s.resolution.id as idScreenResolution
            , s.material
            , s.technology
    FROM Screen s
    where s.id = :id
    """)
    Optional<ADProductScreenResponse> getScreenById(@Param("id") String id);

}
