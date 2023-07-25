package com.likelion.market.controller;

import com.likelion.market.dto.PageDto;
import com.likelion.market.dto.ResponseDto;
import com.likelion.market.dto.SalesItemDto;
import com.likelion.market.dto.UserDto;
import com.likelion.market.service.SalesItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class SalesItemController {
    private final SalesItemService service;

    @PostMapping
    public ResponseDto create(@RequestBody SalesItemDto.CreateAndUpdateRequest requestDto) {
        return service.createItem(requestDto);
    }

    @GetMapping
    public PageDto<SalesItemDto.ReadAllResponse> readAll(
            @RequestParam(value = "page", defaultValue = "1") Integer pageNumber,
            @RequestParam(value = "limit", defaultValue = "25") Integer pageSize
    ) {
        return service.readItemPaged(pageNumber > 0 ? pageNumber - 1 : 0, pageSize);
    }

    @GetMapping("/{itemId}")
    public SalesItemDto.ReadByIdResponse read(@PathVariable("itemId") Long itemId) {
        return service.readItemById(itemId);
    }

    @PutMapping(value = "/{itemId}/image", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseDto updateImage(
            @PathVariable("itemId") Long itemId,
//            @RequestParam("writer") String writer,
//            @RequestParam("password") String password,
            @RequestPart("user") UserDto requestDto,
            @RequestPart("image") MultipartFile itemImage
    ){
        log.info("writer : {}, password : {}, image : {}", requestDto.getWriter(), requestDto.getPassword(), itemImage);
        return service.updateItemImage(itemId, requestDto, itemImage);
    }

    @PutMapping("/{itemId}")
    public ResponseDto updateItem(@PathVariable("itemId") Long itemId, @RequestBody SalesItemDto.CreateAndUpdateRequest requestDto) {
        return service.updateItem(itemId, requestDto);
    }

    @PutMapping("/{itemId}/user")
    public ResponseDto updateUser(@PathVariable("itemId") Long itemId, @RequestBody UserDto.UpdateUserRequest requestDto){
        return service.updateUser(itemId, requestDto);
    }

//    @PutMapping(value = "/{itemId}", params = {"writer", "password"})
//    public ResponseDto updateUser(
//            @PathVariable("itemId") Long itemId,
//            @RequestParam(value = "writer", required = true) String writer,
//            @RequestParam(value = "password", required = true) String password,
//            @RequestBody SalesItemDto.UpdateUserRequest requestDto
//    ) {
//        return service.updateUser(itemId, writer, password, requestDto);
//    }

    @DeleteMapping("/{itemId}")
    public ResponseDto deleteItem(@PathVariable("itemId") Long itemId, @RequestBody UserDto requestDto) {
        return service.deleteItem(itemId, requestDto);
    }
}


