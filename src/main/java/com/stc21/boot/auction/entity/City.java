package com.stc21.boot.auction.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@RequiredArgsConstructor
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_city_sequence")
    @SequenceGenerator(
            name = "pk_city_sequence",
            sequenceName = "city_id_seq",
            initialValue = 1,
            allocationSize = 1)
    private Long id;

    private String name;

    @Column(columnDefinition = "boolean default false", nullable = false)
    private boolean deleted;
}
