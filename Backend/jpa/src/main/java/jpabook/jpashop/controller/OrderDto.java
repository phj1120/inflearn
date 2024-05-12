package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDto {
    private long memberId;
    private long itemId;
    private int count;

}
