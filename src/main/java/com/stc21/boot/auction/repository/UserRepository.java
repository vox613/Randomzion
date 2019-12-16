package com.stc21.boot.auction.repository;

import com.stc21.boot.auction.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByPhoneNumber(String phoneNumber);
    Long findUserByUsername(String username);

    Page<User> findByDeletedFalse(Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE User u SET u.deleted = :isDeleted WHERE u.id = :userId")
    int updateDeletedTo(@Param("userId") Long userId, @Param("isDeleted") boolean isDeleted);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE User u SET u.wallet = :newAmount WHERE u.id = :userId")
    void updateWalletTo(@Param("userId") Long userId, @Param("newAmount") Long newAmount);
}
