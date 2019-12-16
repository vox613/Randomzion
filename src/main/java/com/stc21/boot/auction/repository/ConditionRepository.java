package com.stc21.boot.auction.repository;

import com.stc21.boot.auction.entity.Condition;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConditionRepository extends JpaRepository<Condition, Long> {
    List<Condition> findByDeletedFalse(Sort sort);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Condition c SET c.deleted = :isDeleted WHERE c.id = :categoryId")
    int updateDeletedTo(@Param("categoryId") Long categoryId, @Param("isDeleted") boolean isDeleted);
}
