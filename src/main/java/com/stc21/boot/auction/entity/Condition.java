package com.stc21.boot.auction.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@RequiredArgsConstructor
@Entity
@Data
public class Condition {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_condition_sequence")
    @SequenceGenerator(
            name = "pk_condition_sequence",
            sequenceName = "condition_id_seq",
            initialValue = 1,
            allocationSize = 1)
    private long id;

    @Column
    private String name;

    @Column(columnDefinition = "boolean default false", nullable = false)
    private boolean deleted;
}
