package com.stc21.boot.auction.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@Entity
@NoArgsConstructor
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_photo_sequence")
    @SequenceGenerator(
            name = "pk_photo_sequence",
            sequenceName = "photo_id_seq",
            initialValue = 1,
            allocationSize = 1)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lot_id", nullable = false)
    private Lot lot;

    @Column
    private String url;

    public Photo(String url) {
        this.url = url;
    }

    @Column(columnDefinition = "boolean default false", nullable = false)
    private Boolean deleted;
}
