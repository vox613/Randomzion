package com.stc21.boot.auction.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@RequiredArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_category_sequence")
    @SequenceGenerator(
            name = "pk_category_sequence",
            sequenceName = "category_id_seq",
            initialValue = 1,
            allocationSize = 1)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "boolean default false", nullable = false)
    private boolean deleted;
}
