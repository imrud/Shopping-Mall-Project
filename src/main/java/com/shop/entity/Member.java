package com.shop.entity;

import com.shop.constant.Role;
import com.shop.dto.MemberFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Table(name="member")
@Getter @Setter
@ToString
public class Member extends BaseEntity {


    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Column(unique = true)          // 회원은 이메일 통해 유일하게 구분해야함 -> 동일한 값 db에 들어올 수 없도록 unique로 설정
    private String email;

    private String password;

    private String address;

    @Enumerated(EnumType.STRING)    // enum 타입을 엔티티의 속성으로 지정할 수 있음.
    private Role role;

    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){
        // member 엔티티 생성하는 메소드
        // Member 엔티티에 회원을 생성하는 메소드 만들어서 관리하면 코드가 변경되더라도 한 군데만 수정하면 되는 이점 있음

        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setAddress(memberFormDto.getAddress());
        // 스프이 시큐리티 설정 클래스에 등록한 BCryptPasswordEncoder Bean 을 파라미터로 넘겨서 비밀번호를 암호화
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(password);
        //member.setRole(Role.USER);
        member.setRole(Role.ADMIN);     // 관리자 역할로 생성
        return member;
    }
}
