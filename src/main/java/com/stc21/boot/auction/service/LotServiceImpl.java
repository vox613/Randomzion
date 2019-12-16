package com.stc21.boot.auction.service;

import com.stc21.boot.auction.dto.LotDto;
import com.stc21.boot.auction.dto.UserDto;
import com.stc21.boot.auction.entity.Lot;
import com.stc21.boot.auction.entity.Photo;
import com.stc21.boot.auction.entity.Purchase;
import com.stc21.boot.auction.entity.User;
import com.stc21.boot.auction.exception.NotEnoughMoneyException;
import com.stc21.boot.auction.repository.LotRepository;
import com.stc21.boot.auction.repository.PhotoRepository;
import com.stc21.boot.auction.repository.PurchaseRepository;
import com.stc21.boot.auction.repository.UserRepository;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.extras.springsecurity5.auth.Authorization;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.*;

@Service
public class LotServiceImpl implements LotService {

    // число карточек на странице
    public static final int SIZE = 5;
    private final ModelMapper modelMapper;
    private final LotRepository lotRepository;
    private final PurchaseRepository purchaseRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final GoogleDriveService googleDriveService;
    private final PhotoRepository photoRepository;
    public LotServiceImpl(ModelMapper modelMapper, LotRepository lotRepository, PurchaseRepository purchaseRepository,
                          UserRepository userRepository, UserService userService, GoogleDriveService googleDriveService,
                          PhotoRepository photoRepository) {
        this.modelMapper = modelMapper;
        this.lotRepository = lotRepository;
        this.purchaseRepository = purchaseRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.googleDriveService = googleDriveService;
        this.photoRepository = photoRepository;
    }

    @Override
    public Page<LotDto> getPaginated(Pageable pageable) {
        PageRequest pageRequest = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSortOr(Sort.unsorted()));

        return lotRepository.findByDeletedFalse(pageRequest).map(this::convertToDto);
    }

    @Override
    public Page<LotDto> getPaginatedEvenDeleted(Pageable pageable) {
        PageRequest pageRequest = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSortOr(Sort.unsorted()));

        return lotRepository.findAll(pageRequest).map(this::convertToDto);
    }

    @Override
    public List<Lot> getAllLots() {
        return lotRepository.findAll();
    }

    @Override
    public List<Lot> getAllLotsByUsername(Authentication token) {
        UserDto user = userService.findByUsername(token.getName());
        return lotRepository.findAllByUserUsername(user.getUsername());
    }

    @Override
    @Transactional
    public void updateAllLots(List<Lot> lots) {
        lots.forEach(lot -> {
            lotRepository.updateCurrentPrice(calcCurrentPrice(lot), lot.getId());
        });
    }

    @Override
    public Page<LotDto> getPageOfHomePageLots(int page) {
        PageRequest pageRequest = PageRequest.of(page, SIZE);
//        Page<Lot> lots = lotRepository.findByDeletedFalse(pageRequest);
        Page<Lot> lots = lotRepository.findByDeletedFalse(pageRequest);
        return lots.map(this::convertToLotDto);
    }

    @Override
    public Page<LotDto> getPageOfHomePageLots(int page, Authentication token) {
        PageRequest pageRequest = PageRequest.of(page, SIZE);
//        Page<Lot> lots = lotRepository.findByDeletedFalse(pageRequest);
        Page<Lot> lots = lotRepository.getByDeletedFalseWhereUserUsernameNotEquals(pageRequest, token.getName());
        return lots.map(this::convertToLotDto);
    }


    @SneakyThrows
    @Override
    public Lot saveNewLot(LotDto lotDto, Authentication token, MultipartFile[] images) {
        UserDto authed = userService.findByUsername(token.getName());
        lotDto.setUserDto(authed);
        LocalDateTime nowDateTime = LocalDateTime.now();
        lotDto.setCreationTime(nowDateTime);
        lotDto.setLastModTime(nowDateTime);

        Lot insertedLot = lotRepository.save(convertToEntity(lotDto));

        if ((images.length == 1) && images[0].getOriginalFilename().equals("")) {
            return lotRepository.getOne(insertedLot.getId());
        }

        List<Photo> uploadPhotos = googleDriveService.uploadLotMedia(insertedLot.getId(), images);
        uploadPhotos.forEach(photo -> {
            photo.setLot(insertedLot);
            photo.setDeleted(false);
            photoRepository.save(photo);
        });
        return lotRepository.getOne(insertedLot.getId());
    }

    @Override
    public LotDto findById(long id) {
        Optional<Lot> lot = lotRepository.findById(id);
        LotDto lotDto = null;
        if (lot.isPresent())
            lotDto = convertToLotDto(lot.get());
        return lotDto;
    }

    @Override
    public LotDto findByLotId(Long id) {
        return null;
    }

    /**
     *
     * @param username User.username, which want to buy a lot
     * @param lotDto DTO of lot, which usenrname want to buy
     * @throws NotEnoughMoneyException if User.wallet less than item's cost
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED
            ,isolation = Isolation.SERIALIZABLE)
    public void sale(String username, LotDto lotDto) throws NotEnoughMoneyException {
        User buyer = userRepository.findByUsername(username).orElseThrow(NullPointerException::new);//userService.findByUsername(username);
        Long amount = lotDto.getCurrentPrice();
        if(buyer.getWallet() < amount){
            throw new NotEnoughMoneyException("Недостаточно средств на счету");
        }
        User seller = userRepository.findByUsername(lotDto.getUserDto().getUsername()).orElseThrow(NullPointerException::new);//userService.findByUsername(username);
        Lot boughtLot = lotRepository.findById(lotDto.getId()).orElseThrow(NullPointerException::new);

        seller.setWallet(seller.getWallet() + amount);
        buyer.setWallet(buyer.getWallet() - amount);
        Purchase purchase = new Purchase();
        purchase.setBuyer(buyer);
        purchase.setItem(boughtLot);
        purchase.setPurchaseTime(LocalDateTime.now());
        purchaseRepository.saveAndFlush(purchase);
    }


    private Long calcCurrentPrice(Lot lot) {
        Random random = new Random();
        Long max = lot.getMaxPrice();
        Long min = lot.getMinPrice();
        long randomValue = min + random.nextInt((int) (max - min));
        return randomValue;
    }

    // через мапер преобразуем в DTO. Руками устанавливаем DTO пользователя
    private LotDto convertToLotDto(Lot lot) {
        LotDto lotDto = modelMapper.map(lot, LotDto.class);

        UserDto userDto = modelMapper.map(lot.getUser(), UserDto.class);
        lotDto.setUserDto(userDto);

        for (Photo photo : lot.getPhotos()) {
            if (false == photo.getDeleted()) {
                lotDto.getPhotoUrls().add(photo.getUrl());
            }
        }
        return lotDto;
    }


    @Override
    public LotDto convertToDto(Lot lot) {
        if (lot == null) return null;

        LotDto lotDto = modelMapper.map(lot, LotDto.class);

        lotDto.setUserDto(userService.convertToDto(lot.getUser()));
        lotDto.setCategory(lot.getCategory());
        lotDto.setCity(lot.getCity());
        lotDto.setCondition(lot.getCondition());

        return lotDto;
    }

    private Lot convertToEntity(LotDto lotDto) {
        return modelMapper.map(lotDto, Lot.class);
    }

    @Override
    @Transactional
    public void setDeletedTo(long id, boolean newValue) {
        lotRepository.updateDeletedTo(id, newValue);
    }
}
