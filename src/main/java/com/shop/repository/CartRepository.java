package com.shop.repository;

import com.shop.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    // JpaRepository를 상속받은 CartRepository 인터페이스를 생성

    Cart findByMemberId(Long memberId);
}
