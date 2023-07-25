package com.likelion.market.dto;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PageDto<T> {
    private List<T> content;
    private Integer totalPage;
    private Long totalElements;
    private boolean last;
    private Integer size;
    private Integer number;
    private Integer numberOfElements;
    private boolean first;
    private boolean empty;
    public PageDto<T> makePage(Page<T> originPage) {
        PageDto<T> page = new PageDto<>();
        page.setContent(originPage.getContent());
        page.setTotalPage(originPage.getTotalPages());
        page.setTotalElements(originPage.getTotalElements());
        page.setLast(originPage.isLast());
        page.setSize(originPage.getSize());
        page.setNumber(originPage.getNumber() + 1);
        page.setNumberOfElements(originPage.getNumberOfElements());
        page.setFirst(originPage.isFirst());
        page.setEmpty(originPage.isEmpty());
        return page;
    }
}
