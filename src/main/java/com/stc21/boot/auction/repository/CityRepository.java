package com.stc21.boot.auction.repository;

import com.stc21.boot.auction.entity.City;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    List<City> findByDeletedFalse(Sort sort);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE City c SET c.deleted = :isDeleted WHERE c.id = :cityId")
    int updateDeletedTo(@Param("cityId") Long cityId, @Param("isDeleted") boolean isDeleted);
}
