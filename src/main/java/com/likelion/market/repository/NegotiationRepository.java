package com.likelion.market.repository;

import com.likelion.market.entity.NegotiationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NegotiationRepository extends JpaRepository<NegotiationEntity, Long> {
    Boolean existsByIdAndWriterAndPassword(Long id, String writer, String password); // 해당 제안에 대한 작성자 판별
    Boolean existsByItemIdAndWriterAndPassword(Long itemId, String writer, String password); // 해당 아이템의 제안들 중 해당하는 작성자가 존재하는지 판별
    Boolean existsByItemIdAndStatusLike(Long itemId, String status); // 해당 아이템의 제안 중 status 가 일치하는 제안이 있는지 판별

    List<NegotiationEntity> findAllByItemIdAndStatusIsLike(Long itemId, String status); // 해당 아이템의 제한 중 status 가 일치하는 제안들을 List 로 반환
    Optional<NegotiationEntity> findByItemIdAndId(Long itemId, Long id); // itemId 와 id 가 일치하는 제안 Optional 로 반환
    Page<NegotiationEntity> findAllByItemId(Long itemId, Pageable pageable); // 해당 아이템의 제안 page 반환
    Page<NegotiationEntity> findAllByItemIdAndWriterAndPassword(Long itemId, String writer, String password, Pageable pageable); // 해당 아이템의 제안 중 요청한 작성자의 제한 page 반환
}
