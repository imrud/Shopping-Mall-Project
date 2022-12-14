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

    public static OrderItem createOrderItem(Item item, int count){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);    // 주문할 상품 셋팅
        orderItem.setCount(count);  // 주문 수량 셋팅
        orderItem.setOrderPrice(item.getPrice());   // 현재 시간 기준으로 상품 가격을 주문 가격으로 셋팅

        item.removeStock(count);    // 주문 수량만큼 상품의 재고 수량 감소
        return orderItem;
    }

    public int getTotalPrice(){ // 주문 가격*주문 수량 -> 해당 상품을 주문한 총 가격 계산하는 메소드
        return orderPrice*count;
    }

    public void cancel(){
        this.getItem().addStock(count); // 주문 취소 시 주문 수량만큼 상품의 재고를 더해준다.
    }


    //private LocalDateTime regTime;    //삭제

    //private LocalDateTime updateTime;     // 삭제
}
