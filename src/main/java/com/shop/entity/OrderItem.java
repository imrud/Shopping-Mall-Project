package com.shop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class OrderItem extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    // 하나의 상품은 여러 주문 상품으로 들어갈 수 있으니 다대일 단방향 매핑
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    // 한 번 주문 할 때, 여러개 상품 주문 가능하니
    // 주문 상품 엔티티 - 주문 엔티티를 다대일 단방향 매핑을 먼저 설정
    private Order order;

    private int orderPrice; //주문가격

    private int count; //수량

    //private LocalDateTime regTime;    //삭제

    //private LocalDateTime updateTime;     // 삭제
}
