package com.hospi.hospiplus.repository;


import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import com.hospi.hospiplus.model.ProductWard;

public interface ProductWardRepository extends CrudRepository<ProductWard, Integer> {

    //@Query("SELECT pw FROM ProductWard pw WHERE pw.wardId = :wardId AND pw.code = :code")
    @Query("SELECT * FROM `ProductWard` WHERE wardID = :wardId AND code = :code")
    Optional<ProductWard> findByWardIdAndCode(Integer wardId, String code);

}

