package com.likelion.market.controller;

import com.likelion.market.dto.NegotiationDto;
import com.likelion.market.dto.PageDto;
import com.likelion.market.dto.ResponseDto;
import com.likelion.market.dto.UserDto;
import com.likelion.market.service.NegotiationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/items/{itemId}/proposals")
@RestController
@RequiredArgsConstructor
public class NegotiationController {
    private final NegotiationService service;

    @PostMapping
    public ResponseDto create(@PathVariable("itemId") Long itemId, @RequestBody NegotiationDto.CreateAndUpdateRequest requestDto) {
        return service.createNegotiation(itemId, requestDto);
    }

    @GetMapping
    public PageDto<NegotiationDto.ReadNegotiationResponse> readAll(
            @PathVariable("itemId") Long itemId,
            @RequestParam("writer") String writer,
            @RequestParam("password") String password,
            @RequestParam(value = "page", defaultValue = "1") Integer pageNumber,
            @RequestParam(value = "limit", defaultValue = "25") Integer pageSize
    ) {
        return service.readNegotiationPaged(itemId, writer, password, pageNumber > 0 ? pageNumber - 1 : 0, pageSize);
    }

    @PutMapping("/{proposalId}")
    public ResponseDto update(@PathVariable("itemId") Long itemId, @PathVariable("proposalId") Long proposalId, @Valid @RequestBody NegotiationDto.CreateAndUpdateRequest requestDto) {
        return service.negotiationUpdate(itemId, proposalId, requestDto);
    }

    @DeleteMapping("/{proposalId}")
    public ResponseDto delete(@PathVariable("itemId") Long itemId, @PathVariable("proposalId") Long proposalId, @RequestBody UserDto requestDto) {
        return service.negotiationDelete(itemId, proposalId, requestDto);
    }

    // update User 추가
}
