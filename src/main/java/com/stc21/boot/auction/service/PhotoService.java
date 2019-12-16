package com.stc21.boot.auction.service;

import com.stc21.boot.auction.dto.PhotoDto;
import com.stc21.boot.auction.entity.Photo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PhotoService {
    List<PhotoDto> getAllPhotos();
    Page<PhotoDto> getPaginated(Pageable pageable);
    Page<PhotoDto> getPaginatedEvenDeleted(Pageable pageable);
    PhotoDto convertToDto(Photo photo);

    void insertPhoto(Photo photo);

    void setDeletedTo(long id, boolean newValue);
}
