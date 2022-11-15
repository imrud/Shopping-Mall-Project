package com.shop.repository;

import com.shop.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    // Member 엔티티를 데이터베이스에 저장할 수 있도록 MemberRepository 만들기

    // 회원 가입 시 중복된 회원 검사위해 이메일로 회원을 검살할 수 있도록 쿼리 메소드 작성
    Member findByEmail(String email);
}
