package com.stc21.boot.auction.scheduler;

import com.stc21.boot.auction.entity.Lot;
import com.stc21.boot.auction.service.LotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class LotPriceScheduler {

    private final LotService lotService;

    public LotPriceScheduler(LotService lotService) {
        this.lotService = lotService;
    }

    @Scheduled(cron = "0,30 5 * * * *")
    public void updateLots() {
        log.info("< update Lots");
        List<Lot> lots = lotService.getAllLots();
        lotService.updateAllLots(lots);
        log.info("> update Lots");
    }
}
