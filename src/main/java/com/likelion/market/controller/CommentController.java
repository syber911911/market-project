package com.likelion.market.controller;

import com.likelion.market.dto.CommentDto;
import com.likelion.market.dto.PageDto;
import com.likelion.market.dto.ResponseDto;
import com.likelion.market.dto.UserDto;
import com.likelion.market.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/items/{itemId}/comments")
public class CommentController {
    private final CommentService service;

    @PostMapping
    public ResponseDto create(@PathVariable("itemId") Long itemId, @RequestBody CommentDto.CreateAndUpdateCommentRequest requestDto) {
        return service.createComment(itemId, requestDto);
    }

    @GetMapping
    public PageDto<CommentDto.ReadCommentsResponse> readAll(
            @PathVariable("itemId") Long itemId,
            @RequestParam(value = "page", defaultValue = "1") Integer pageNumber,
            @RequestParam(value = "limit", defaultValue = "25") Integer pageSize
    ) {
        return service.readCommentPaged(itemId, pageNumber > 0 ? pageNumber - 1 : 0, pageSize);
    }

    @PutMapping("/{commentId}")
    public ResponseDto update(
            @PathVariable("itemId") Long itemId,
            @PathVariable("commentId") Long commentId,
            @RequestBody CommentDto.CreateAndUpdateCommentRequest requestDto
    ) {
        return service.updateComment(itemId, commentId, requestDto);
    }

    @PutMapping("/{commentId}/reply")
    public ResponseDto updateReply(
            @PathVariable("itemId") Long itemId,
            @PathVariable("commentId") Long commentId,
            @RequestBody CommentDto.UpdateReplyRequest requestDto
    ) {
        return service.updateReply(itemId, commentId, requestDto);
    }

    @PutMapping("/{commentId}/user")
    public ResponseDto updateUser(@PathVariable("itemId") Long itemId,
                                  @PathVariable("commentId") Long commentId,
                                  @RequestBody UserDto.UpdateUserRequest requestDto
    ){
        return service.updateUser(itemId, commentId, requestDto);
    }
    @DeleteMapping("/{commentId}")
    public ResponseDto delete(
            @PathVariable("itemId") Long itemId,
            @PathVariable("commentId") Long commentId,
            @RequestBody UserDto requestDto
    ){
        return service.deleteComment(itemId, commentId, requestDto);
    }
}
