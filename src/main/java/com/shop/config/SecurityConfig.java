package com.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.shop.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    MemberService memberService;

    /*@Override
    protected void configure(HttpSecurity http) throws Exception {
        // http 요청에 대한 보안 설정. 페이지 권한 설정, 로그인 페이지 설정, 로그아우 ㅅ메소드 등에 대한 설정을 작성
        //http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
    }*/

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.formLogin()
                .loginPage("/members/login")        // 로그인 페이지 url 설정
                .defaultSuccessUrl("/")         // 로그인 성공시 이동할 url 설정
                .usernameParameter("email")     // 로그인 시 사용할 파라미터 이름으로 email 지정
                .failureUrl("/members/login/error")         // 로그인 실패시 이동할 url 설정
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout")) // 로그아웃 url 설정
                .logoutSuccessUrl("/")     // 로그아웃 성공시 이동할 url 설정
        ;

        http.authorizeRequests()
                .mvcMatchers("/", "/members/**", "/item/**", "/images/**").permitAll()
                .mvcMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
        ;

        http.exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
        ;

    }

    @Override
    public void configure(WebSecurity web) throws Exception{
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**");     // 하위 파일은 인증 무시하도록 설정
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        // pw를 데이터베이스에 그대로 저장했을 때, db가 해킹당하면 고객의 회원 정보가 그대로 노출된다.
        // 이를 해결하기 위해 BCryptPasswordEncoderd의 해시 함수 이용하여 비밀번호를 아모화 하여 저장
        // BCryptPasswordEncoder를 빈으로 등록하여 사용
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        // userDetailsService 를 구현하고 있는 객체로 memberService 를 지정해주며,
        // 비밀번호 암호화를 위해 passwordEncoder 를 지정해준다.
        auth.userDetailsService(memberService)
                .passwordEncoder(passwordEncoder());
    }
}
