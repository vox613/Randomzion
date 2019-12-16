package com.stc21.boot.auction.repository;

import com.stc21.boot.auction.entity.Category;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByDeletedFalse(Sort sort);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Category c SET c.deleted = :isDeleted WHERE c.id = :categoryId")
    int updateDeletedTo(@Param("categoryId") Long categoryId, @Param("isDeleted") boolean isDeleted);
}
