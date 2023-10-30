package com.kkoch.admin.api.service.auction;

import com.kkoch.admin.api.controller.auction.response.AuctionTitleResponse;
import com.kkoch.admin.api.service.auction.dto.AddAuctionDto;
import com.kkoch.admin.api.service.auction.dto.SetAuctionDto;
import com.kkoch.admin.domain.admin.Admin;
import com.kkoch.admin.domain.auction.Auction;
import com.kkoch.admin.domain.auction.Status;
import com.kkoch.admin.domain.auction.repository.AuctionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
@Transactional
public class AuctionService {

    private final AuctionRepository auctionRepository;

    public AuctionTitleResponse addAuction(Long adminId, AddAuctionDto dto) {
        int code = dto.getCode();

        if (isRangeBetweenOneToFour(code)) {
            throw new IllegalArgumentException("구분코드 에러");
        }

        Admin admin = Admin.toEntity(adminId);

        Auction auction = Auction.toEntity(dto.getCode(), dto.getStartTime(), admin);

        Auction savedAuction = auctionRepository.save(auction);

        return AuctionTitleResponse.of(savedAuction);
    }

    public AuctionTitleResponse setAuction(Long auctionId, SetAuctionDto dto) {
        int code = dto.getCode();

        if (isRangeBetweenOneToFour(code)) {
            throw new IllegalArgumentException("구분코드 에러");
        }

        Auction auction = getAuctionEntity(auctionId);

        auction.changeAuction(dto.getCode(), dto.getStartTime());

        return AuctionTitleResponse.of(auction);
    }

    public AuctionTitleResponse setStatus(Long auctionId, Status status) {
        Auction auction = getAuctionEntity(auctionId);
        auction.changeStatus(status);
        return AuctionTitleResponse.of(auction);
    }

    public Long remove(Long auctionId) {
        Auction auction = getAuctionEntity(auctionId);
        auction.remove();
        return auction.getId();
    }

    private boolean isRangeBetweenOneToFour(int code) {
        return code < 0 || code > 4;
    }

    private Auction getAuctionEntity(Long auctionId) {
        return auctionRepository.findById(auctionId)
            .orElseThrow(() -> new NoSuchElementException("존재하지 않는 경매 일정"));
    }
}
