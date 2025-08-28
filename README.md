# Spring Security Study 🔐
Spring Security 학습을 위한 인증/인가 시스템 구현 프로젝트  
[📋 학습정리 보러가기](https://livoi.notion.site/8-19627f69d409808482ade6b0e92c379e)

## 구현 내용

기본 로그인 - 사용자명/비밀번호 인증  
JWT 토큰 - Access Token + Refresh Token  
OAuth2 - Google 로그인  
권한 관리 - USER / MANAGER / ADMIN 역할별 접근 제어  

## 기술 스택
Spring Boot 3.4.5 | Spring Security 6 | JWT | OAuth2 | MySQL | JPA

## 실행 방법

DB 설정 - MySQL 실행 (포트: 3312)  
OAuth 설정 - Google Client ID/Secret 환경변수 설정  
실행 - ./gradlew bootRun  

## 주요 엔드포인트

`POST /api/auth/signup` - 회원가입  
`POST /api/auth/login` - 로그인  
`GET /oauth2/authorization/google` - Google 로그인
`GET /api/users` - 권한별로 제한된 사용자 목록 조회
