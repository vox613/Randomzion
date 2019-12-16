package com.stc21.boot.auction.repository;

import com.stc21.boot.auction.entity.Purchase;
import com.stc21.boot.auction.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    @Modifying(clearAutomatically = true)
    @Query("UPDATE User u SET u.deleted = :isDeleted WHERE u.id = :userId")
    int updateDeletedTo(@Param("userId") Long userId, @Param("isDeleted") boolean isDeleted);

    List<Purchase> findAllByBuyer (User buyer);
}
