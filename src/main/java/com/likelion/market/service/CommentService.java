package com.likelion.market.service;

import com.likelion.market.dto.CommentDto;
import com.likelion.market.dto.PageDto;
import com.likelion.market.dto.ResponseDto;
import com.likelion.market.dto.UserDto;
import com.likelion.market.entity.CommentEntity;
import com.likelion.market.entity.SalesItemEntity;
import com.likelion.market.repository.CommentRepository;
import com.likelion.market.repository.SalesItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final SalesItemRepository salesItemRepository;

    // create comment
    public ResponseDto createComment(Long itemId, CommentDto.CreateAndUpdateCommentRequest requestDto) {
        if (!salesItemRepository.existsById(itemId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND); // 아이템 존재하지 않음

        CommentEntity comment = new CommentEntity();
        comment.setItemId(itemId);
        comment.setWriter(requestDto.getWriter());
        comment.setPassword(requestDto.getPassword());
        comment.setContent(requestDto.getContent());
        commentRepository.save(comment);

        ResponseDto response = new ResponseDto();
        response.setMessage("댓글이 등록되었습니다.");
        return response;
    }

    // readAll comment
    public PageDto<CommentDto.ReadCommentsResponse> readCommentPaged(Long itemId, Integer pageNumber, Integer pageSize) {
        if (!salesItemRepository.existsById(itemId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND); // 아이템 존재하지 않음

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("id"));
        Page<CommentEntity> commentEntityPage = commentRepository.findAllByItemId(itemId, pageable);
        Page<CommentDto.ReadCommentsResponse> originCommentDtoPage = commentEntityPage.map(CommentDto.ReadCommentsResponse::fromEntity);
        PageDto<CommentDto.ReadCommentsResponse> pageDto = new PageDto<>();
        return pageDto.makePage(originCommentDtoPage);
    }

    // update comment
    public ResponseDto updateComment(Long itemId, Long commentId, CommentDto.CreateAndUpdateCommentRequest requestDto) {
        if (!salesItemRepository.existsById(itemId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND); // 아이템 존재하지 않음

        Optional<CommentEntity> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isPresent()) {
            CommentEntity comment = optionalComment.get();
            if (comment.getItemId().equals(itemId)) {
                if (comment.getWriter().equals(requestDto.getWriter()) && comment.getPassword().equals(requestDto.getPassword())) {
                    comment.setContent(requestDto.getContent());
                    commentRepository.save(comment);

                    ResponseDto response = new ResponseDto();
                    response.setMessage("댓글이 수정되었습니다.");
                    return response;
                } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED); // 인증 오류
            } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST); // 댓글이 해당 아이템의 댓글이 아님
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND); // 댓글 존재하지 않음
    }

    // update reply
    public ResponseDto updateReply(Long itemId, Long commentId, CommentDto.UpdateReplyRequest requestDto) {
        Optional<SalesItemEntity> optionalItem = salesItemRepository.findById(itemId);
        SalesItemEntity item;

        if (optionalItem.isPresent()) {
            item = optionalItem.get();
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND); // 아이템 존재하지 않음

        Optional<CommentEntity> optionalComment = commentRepository.findById(commentId);

        if (optionalComment.isPresent()) {
            CommentEntity comment = optionalComment.get();
            if (comment.getItemId().equals(itemId)) {
                if (item.getWriter().equals(requestDto.getWriter()) && item.getPassword().equals(requestDto.getPassword())) {
                    comment.setReply(requestDto.getReply());
                    commentRepository.save(comment);

                    ResponseDto response = new ResponseDto();
                    response.setMessage("댓글에 답변이 추가되었습니다.");
                    return response;
                } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED); // 인증 오류
            } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST); // 댓글이 해당 아이템의 댓글이 아님
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND); // 댓글 존재하지 않음
    }

    // update user
    public ResponseDto updateUser(Long itemId, Long commentId, UserDto.UpdateUserRequest requestDto) {
        if (!salesItemRepository.existsById(itemId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND); // 아이템 존재하지 않음

        Optional<CommentEntity> optionalComment = commentRepository.findById(commentId);

        if (optionalComment.isPresent()) {
            CommentEntity comment = optionalComment.get();
            if (comment.getItemId().equals(itemId)) {
                if (comment.getWriter().equals(requestDto.getRecentUser().getWriter())
                        && comment.getPassword().equals(requestDto.getRecentUser().getPassword())) {
                    comment.setWriter(requestDto.getUpdateUser().getWriter());
                    comment.setPassword(requestDto.getUpdateUser().getPassword());
                    commentRepository.save(comment);

                    ResponseDto response = new ResponseDto();
                    response.setMessage("댓글 작성자 정보가 수정되었습니다.");
                    return response;
                } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED); // 인증 오류
            } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST); // 댓글이 해당 아이템의 댓글이 아님
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND); // 댓글 존재하지 않음
    }

    // delete
    public ResponseDto deleteComment(Long itemId, Long commentId, UserDto requestDto) {
        if (!salesItemRepository.existsById(itemId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND); // 아이템 존재하지 않음

        Optional<CommentEntity> optionalComment = commentRepository.findById(commentId);

        if (optionalComment.isPresent()) {
            CommentEntity comment = optionalComment.get();
            if (comment.getItemId().equals(itemId)) {
                if (comment.getWriter().equals(requestDto.getWriter()) && comment.getPassword().equals(requestDto.getPassword())) {
                    commentRepository.delete(comment);

                    ResponseDto response = new ResponseDto();
                    response.setMessage("댓글을 삭제했습니다.");
                    return response;
                } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED); // 인증 오류
            } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST); // 댓글이 해당 아이템의 댓글이 아님
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND); // 댓글 존재하지 않음
    }
}
