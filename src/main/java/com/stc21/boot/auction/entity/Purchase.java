package com.stc21.boot.auction.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_purchase_sequence")
    @SequenceGenerator(
            name = "pk_purchase_sequence",
            sequenceName = "purchase_id_seq",
            initialValue = 1,
            allocationSize = 1)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "buyer_id", nullable = false, updatable = false)
    private User buyer;

    @Column
    private LocalDateTime purchaseTime;

    @OneToOne(optional = false, fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "lot_id", nullable = false, updatable = false)
    private Lot item;

}