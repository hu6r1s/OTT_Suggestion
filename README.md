# 개요

- 프로젝트 이름: OTT 추천 커뮤니티
- 프로젝트 설명: 사용자들이 시청한 영화 및 드라마를 다른 사용자들에게 소개하고 함께 소통할 수 있는 커뮤니티를 제공하는 플랫폼입니다. (사용자들은 서로의 경험을 공유하며 새로운 작품을 발견하는 소통의 장을 제공합니다.)
    - 주요 기능
        - 영화 및 드라마 소개(게시글 작성, 이미지 삽입)
        - 커뮤니티 기능(팔로우, 댓글, 좋아요)
        - 랭킹 시스템 (좋아요를 가장 많이 받은 게시글 3가지를 웹 페이지 상단에 노출합니다.)
- 프로젝트 기간: 2024.02.07 - 2024.02.15
- 팀명 : threethreethreethree
- 멤버 : 3조 (팀장 정현준, 임현태, 박희은, 조우석, 송선호)

## 구현 목표

- 필수 기능 구현
- 이메일 인증
- 좋아요 기능
- TOP3 좋아요 게시물 조회
- 이미지 업로드
- 팔로우 기능
- ~~테스트 코드 작성~~
- SOFT DELETE 방식
- Refresh Token

# 프로젝트 설계

## 와이어 프레임

![image](https://github.com/threethreethreethree/OTT_Suggestion/assets/67190090/e5850fca-15d2-4f23-8dde-1de4ac72ee19)


## API 명세

[API 명세 링크](https://www.notion.so/d24c590d55e645bba7523e67051b4fd9?v=4ab5a98eaa7e47a3accfd35cd0113595&pvs=4)

## ERD(확인 부탁🙏🏻)

![image](https://github.com/threethreethreethree/OTT_Suggestion/assets/67190090/aeec954b-c90f-4288-82e0-04a240453361)

## 환경 변수
```
spring.datasource.url=jdbc:mysql://localhost:3306/ott
spring.datasource.username=username
spring.datasource.password=password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update

spring.jpa.properties.hibernate.show_sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.use_sql_comments=true

jwt.secret.key=secretkey

spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your email
spring.mail.password=your password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

upload.path=path
defaultImage.path=path
```
