package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookForm {

    // 상품 수정 때문에 id값이 있어야함
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;
    private String author;
    private String isbn;
}
