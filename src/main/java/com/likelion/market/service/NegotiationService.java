package com.likelion.market.service;

import com.likelion.market.dto.NegotiationDto;
import com.likelion.market.dto.PageDto;
import com.likelion.market.dto.ResponseDto;
import com.likelion.market.dto.UserDto;
import com.likelion.market.entity.NegotiationEntity;
import com.likelion.market.entity.SalesItemEntity;
import com.likelion.market.repository.NegotiationRepository;
import com.likelion.market.repository.SalesItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NegotiationService {
    private final SalesItemRepository salesItemRepository;
    private final NegotiationRepository negotiationRepository;

    // create suggest
    public ResponseDto createNegotiation(Long itemId, NegotiationDto.CreateAndUpdateRequest requestDto) {
        if (!salesItemRepository.existsById(itemId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND); // 아이템 존재하지 않음

        NegotiationEntity negotiation = new NegotiationEntity();
        negotiation.setItemId(itemId);
        negotiation.setSuggestedPrice(requestDto.getSuggestedPrice());
        negotiation.setStatus("제안");
        negotiation.setWriter(requestDto.getWriter());
        negotiation.setPassword(requestDto.getPassword());
        negotiationRepository.save(negotiation);

        ResponseDto response = new ResponseDto();
        response.setMessage("구매 제안이 등록되었습니다.");
        return response;
    }

    // read
    public PageDto<NegotiationDto.ReadNegotiationResponse> readNegotiationPaged(
            Long itemId,
            String writer,
            String password,
            Integer pageNumber,
            Integer pageSize
    ) {
        if (!salesItemRepository.existsById(itemId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND); // 아이템 존재하지 않음

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("id"));
        if (salesItemRepository.existsByIdAndWriterAndPassword(itemId, writer, password)) {
            // 요청한 사용자가 물품 등록자
            // 해당 아이템에 대한 모든 제안 read
            Page<NegotiationEntity> negotiationEntityPage = negotiationRepository.findAllByItemId(itemId, pageable);
            Page<NegotiationDto.ReadNegotiationResponse> originNegotiationDtoPage = negotiationEntityPage.map(NegotiationDto.ReadNegotiationResponse::fromEntity);
            PageDto<NegotiationDto.ReadNegotiationResponse> pageDto = new PageDto<>();
            return pageDto.makePage(originNegotiationDtoPage);
        }
        if (negotiationRepository.existsByItemIdAndWriterAndPassword(itemId, writer, password)) {
            // 요청한 사용자가 제안 등록자
            // 요청한 사용자가 작성한 제안만 read
            Page<NegotiationEntity> negotiationEntityPage = negotiationRepository.findAllByItemIdAndWriterAndPassword(itemId, writer, password, pageable);
            Page<NegotiationDto.ReadNegotiationResponse> originNegotiationDtoPage = negotiationEntityPage.map(NegotiationDto.ReadNegotiationResponse::fromEntity);
            PageDto<NegotiationDto.ReadNegotiationResponse> pageDto = new PageDto<>();
            return pageDto.makePage(originNegotiationDtoPage);
        } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        // 제안이 존재하지 않거나
        // 요청한 사용자 정보가 물품 등록자, 제안 등록자 모두 아님
    }

    // update suggest
    public ResponseDto priceUpdate(Long itemId, Long id, NegotiationDto.CreateAndUpdateRequest requestDto) {
        // 해당 제안의 작성자 정보와 Request 에 포함된 작성자 정보가 일치하는 경우
        if (negotiationRepository.existsByIdAndWriterAndPassword(id, requestDto.getWriter(), requestDto.getPassword())) {
            Optional<NegotiationEntity> optionalNegotiation = negotiationRepository.findByItemIdAndId(itemId, id);
            // 해당 아이템에 속한 제안이 맞는 경우
            if (optionalNegotiation.isPresent()) {
                NegotiationEntity negotiation = optionalNegotiation.get();
                negotiation.setSuggestedPrice(requestDto.getSuggestedPrice());
                negotiationRepository.save(negotiation);

                ResponseDto response = new ResponseDto();
                response.setMessage("제안이 수정되었습니다.");
                return response;
            } else throw new ResponseStatusException(HttpStatus.NOT_FOUND); // 해당 아이템의 제안이 아니거나 해당하는 제안이 없음
        } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED); // 인증 오류 및 제안이 존재하지 않음
    }

    // update status
    public ResponseDto statusUpdate(Long itemId, Long id, NegotiationDto.CreateAndUpdateRequest requestDto, SalesItemEntity item) {
        // 확정을 위한 Request 가 아닌 경우
        if (!requestDto.getStatus().equals("확정")) {
            // 물품 등록자의 요청
            // 해당 물품의 작성자 정보와 Request 에 포함된 작성자 정보가 일치하는 경우
            if (salesItemRepository.existsByIdAndWriterAndPassword(itemId, requestDto.getWriter(), requestDto.getPassword())) {
                Optional<NegotiationEntity> optionalNegotiation = negotiationRepository.findByItemIdAndId(itemId, id);
                // 해당 아이템에 속한 제안이 맞는 경우
                if (optionalNegotiation.isPresent()) {
                    NegotiationEntity negotiation = optionalNegotiation.get();
                    // 해당 제안의 상태가 제안 상태이고, 아직 수락된 제안이 없는 경우
                    if (negotiation.getStatus().equals("제안") && !negotiationRepository.existsByItemIdAndStatusLike(itemId, "수락")) {
                        negotiation.setStatus(requestDto.getStatus());
                        negotiationRepository.save(negotiation);

                        ResponseDto response = new ResponseDto();
                        response.setMessage("제안의 상태가 변경되었습니다.");
                        return response;
                    } else
                        throw new ResponseStatusException(HttpStatus.IM_USED); // 이미 해당 제안이 수락, 거절 또는 확정 상태 | 이미 수락 상태인 제안이 있음
                } else throw new ResponseStatusException(HttpStatus.NOT_FOUND); // 해당 아이템의 제안이 아니거나 해당하는 제안이 없음
            } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED); // 인증 오류 및 제안이 존재하지 않음
        }
        // 확정을 위한 Request 인 경우
        // 제안 등록자의 요청
        // 해당 제안의 작성자 정보와 Request 에 포함된 작성자 정보가 일치하는 경우
        if (negotiationRepository.existsByIdAndWriterAndPassword(id, requestDto.getWriter(), requestDto.getPassword())) {
            Optional<NegotiationEntity> optionalNegotiation = negotiationRepository.findByItemIdAndId(itemId, id);
            // 해당 아이템에 속한 제안이 맞는 경우
            if (optionalNegotiation.isPresent()) {
                NegotiationEntity negotiation = optionalNegotiation.get();
                // 해당 제안이 수락 상태인 경우
                if (negotiation.getStatus().equals("수락")) {
                    negotiation.setStatus(requestDto.getStatus());
                    negotiationRepository.save(negotiation);

                    // 아이템의 상태 update
                    item.setStatus("판매 완료");
                    salesItemRepository.save(item);

                    // 아직 제안 상태인 제안 List
                    // 거절 상태 update
                    List<NegotiationEntity> remainderNegotiations = negotiationRepository.findAllByItemIdAndStatusIsLike(itemId, "제안");
                    // 제안 상태인 제안이 존재하는 경우
                    if (!remainderNegotiations.isEmpty()) {
                        for (NegotiationEntity element : remainderNegotiations) {
                            element.setStatus("거절");
                        }
                        negotiationRepository.saveAll(remainderNegotiations);
                    }

                    ResponseDto response = new ResponseDto();
                    response.setMessage("구매가 확정되었습니다.");
                    return response;
                } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST); // 제안이 현재 수락 상태가 아님
            } else throw new ResponseStatusException(HttpStatus.NOT_FOUND); // 해당 아이템의 제안이 아님
        } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED); // 인증 오류 및 제안이 존재하지 않음
    }

    public ResponseDto negotiationUpdate(Long itemId, Long id, NegotiationDto.CreateAndUpdateRequest requestDto) {
        Optional<SalesItemEntity> optionalItem = salesItemRepository.findById(itemId);
        if (optionalItem.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND); // 아이템 존재하지 않음

        SalesItemEntity item = optionalItem.get();
        if (requestDto.getSuggestedPrice() != null) {
            // 가격 수정
            return priceUpdate(itemId, id, requestDto);
        }

        if (requestDto.getStatus() != null) {
            // 상태 수정
            return statusUpdate(itemId, id, requestDto, item);
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST); // 가격, 상태 수정 그 어떤 것도 아닌 경우
    }

    // delete suggest
    public ResponseDto negotiationDelete(Long itemId, Long id, UserDto requestDto) {
        if (!salesItemRepository.existsById(itemId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND); // 아이템 존재하지 않음

        // 해당 제안의 작성자 정보와 Request 에 포함된 작성자 정보가 일치하는 경우
        if (negotiationRepository.existsByIdAndWriterAndPassword(id, requestDto.getWriter(), requestDto.getPassword())) {
            Optional<NegotiationEntity> optionalNegotiation = negotiationRepository.findByItemIdAndId(itemId, id);
            // 해당 아이템에 속한 제안이 맞는 경우
            if (optionalNegotiation.isPresent()) {
                NegotiationEntity negotiation = optionalNegotiation.get();
                negotiationRepository.delete(negotiation);

                ResponseDto response = new ResponseDto();
                response.setMessage("제안을 삭제했습니다.");
                return response;
            } else throw new ResponseStatusException(HttpStatus.NOT_FOUND); // 해당 아이템의 제안이 아니거나 해당하는 제안이 없음
        } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED); // 인증 오류 및 제안이 존재하지 않음
    }

    // update user 추가
}
