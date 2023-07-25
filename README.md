# 멋쟁이 사자처럼 백엔트 스쿨 5기 - LEEHEEJUN
# ♻️멋사마켓♻️
Spring Boot Mini Project - 중고 거래 플랫폼 api

## 📒프로젝트 개요
중고 거래 판매자가 물품을 등록하고, 댓글을 통해 구매자와 소통, 구매 제안과 수락 할 수 있는 기능을 구현

## 🗓️ 프로젝트 기간
2023-06-29 ~ 2023-07-05

## 🧑🏻‍💻 개발환경
- `java version 17`
- `Spring Boot 3.1.1`
- `SQLite`
- `IntelliJ`
- `MacOS`

## ⚙️ 구현 기능
### DB ERD
<img src="https://github.com/likelion-backend-5th/MiniProject_Basic_LeeHeejun/assets/64578367/83890da0-83b5-455f-88e0-5ed1d1ca9ecb" width="500" height="500">

### 중고 물품
 기능 | HTTP METHOD | URL
 --- | ----------- | ---
 물품 등록 | POST | /items
 물품 조회 | GET | /items/{itemId}
 물품 전체 조회 | GET | /items?page={page}&limit={limit}
 물품 수정 | PUT | /items/{itemId}
 물품 이미지 등록 및 수정 | PUT | /items/{itemId}/image
 물품 삭제 | DELETE | /items/{itemId}
 물품 등록 사용자 정보 수정 | PUT | /items/{itemId}
 
#### ResponseDto
간단한 Response Message 를 담기 위한 DTO

#### SalesItemDto
댓글에 관한 사용자 요청과 응답 표현을 위한 DTO, 요구 사항 마다 Request 와 Response 가 달라 inner class 를 활용

#### UserDto
작성자와 비밀번호를 담는 UserDto
작성자 정보 수정 기능을 위해 제작

#### PageDto
페이지 단위 조회를 하는 경우 Page 객체의 정보를 선택적으로 제공하기 위해 제작

#### 등록, 조회, 수정, 삭제, 이미지 등록, 작성자 정보 수정
- 등록
  - 물품 등록, 제목 / 설명 / 가격 / 작성자 / 비밀번호는 필수 사항
- 조회
  - 물품 ID 를 기반으로 단일 물품 조회
  - 전체 물품 조회 ( 페이지 단위 조회 )
- 수정
  - 수정을 시도하는 경우 물품을 등록할 때 등록한 작성자 및 비밀번호를 포함해야 한다
  - 조건문을 이용한 작성자 정보 검증
  - 작성자 정보 및 비밀번호를 제외한 제목 / 설명 / 가격 수정은 가능하다
- 삭제
  - 삭제를 시도하는 경우 물품을 등록할 때 등록한 작성자 및 비밀번호를 포함해야 한다
  - 조건문을 이용한 작성자 정보 검증
- 이미지 등록
  - 이미지 등록을 시도하는 경우 물품을 등록할 때 등록한 작성자 및 비밀번호를 포함해야 한다
  - 조건문을 이용한 작성자 정보 검증
  - 등록된 물품 정보에 이미지 첨부 가능
  - 이미지는 프로젝트의 src/main/resources/images/ 에 TimeStamp Data_writer.png 형태로 저장된다
  - 기본 static-path 는 /static/images/**
- 작성자 정보 수정
  - 등록된 물품을 작성한 작성자 정보를 수정할 수 있다
  - 물품을 등록할 때 등록한 작성자 및 비밀번호를 포함해야 한다
  - 조건문을 이용한 작성자 정보 검증
  - 1. Query Params 과 writer 와 password 를 표현하는 UserDto 를 이용한 현재 작성자 정보 및 수정할 작성자 정보 표현
  - 2. UserDto recentUser 필드와 UserDto updateUser 필드를 갖는 UserDto inner class 를 제작해 현재 작성자 정보 및 수정할 작성자 정보 표현
  - 해당 프로젝트에서는 방법 2 를 채택 ( Service 에는 두 가지 모두 구현되어 있지만 Controller 에는 하나만 Mapping 되어 있음 )
- 추가적으로 진행할 사항
  - validation 진행
  - 현재는 작성자 정보가 필요한 경우 물품 ID 를 통해 조회한 결과에서 조건문을 통해 작성자 정보를 검증
    - 조회를 할 때 물품 ID, 작성자 정보를 모두 만족하는 레코드를 조회하도록 수정 가능
    - 기존 방식은 물품 ID 에 해당하는 물품이 없는 것인지 작성자 정보가 틀린 것인지 좀 더 세세하게 예외처리 가능

### 댓글
기능 | HTTP METHOD | URL
 --- | ----------- | ---
 댓글 등록 | POST | /items/{itemId}/comments
 댓글 전체 조회 | GET | items/{itemId}/comments?page={page}&limit={limit}
 댓글 수정 | PUT | /items/{itemId}/comments/{commentId}
 답글 등록 및 수정 | PUT | /items/{itemId}/comments/{commentId}/reply
 댓글 삭제 | DELETE | /items/{itmeId}/comments/{commentId}
 댓글 등록 사용자 정보 수정 | PUT | /items/{itemId}/comments/{commentId}/user
 
#### CommentDto
댓글에 관한 사용자 요청과 응답 표현을 위한 DTO, 요구 사항 마다 Request 와 Response 가 달라 inner class 를 활용

#### ResponseDto, PageDto, UserDto 재사용

#### 등록, 조회, 수정, 삭제, 답글 등록 및 수정, 작성자 정보 수정
- 등록
  - 물품 ID 를 기반으로 댓글 등록, 작성자 / 비밀번호 / 내용은 필수 사항
  - 물품 ID 기반으로 해당 물품이 존재하는지 확인
- 조회
  - 물품 ID 를 기반으로 해당 물품이 존재하는지 확인 
  - 물품 ID 를 기반으로 해당 물품 ID 를 레코드에 포함한 전체 댓글 조회 ( 페이지 단위 조회 )
- 수정
  - 물품 ID 를 기반으로 해당 물품이 존재하는지 확인
  - 해당 댓글 ID 를 기반으로 존재하는 댓글인지 확인
  - 조건문을 활용해 해당 댓글이 물품에 속한 댓글인지 확인 
  - 수정을 시도하는 경우 댓글을 등록할 때 등록한 작성자 및 비밀번호를 포함해야 한다
  - 조건문을 이용한 작성자 정보 검증
  - 작성자 정보 및 비밀번호를 제외한 내용 수정은 가능하다
- 답글 등록 및 수정
  - 물품 ID 를 기반으로 해당 물품이 존재하는지 확인
  - 해당 댓글 ID 를 기반으로 존재하는 댓글인지 확인
  - 조건문을 활용해 해당 댓글이 물품에 속한 댓글인지 확인
  - 답글 등록 및 수정을 시도하는 경우 물품을 등록할 때 등록한 작성자 및 비밀번호를 포함해야 한다
  - 조건문을 이용한 물품 작성자 정보 검증
- 삭제
  - 물품 ID 를 기반으로 해당 물품이 존재하는지 확인
  - 해당 댓글 ID 를 기반으로 존재하는 댓글인지 확인
  - 조건문을 활용해 해당 댓글이 물품에 속한 댓글인지 확인
  - 삭제를 시도하는 경우 물품을 등록할 때 등록한 작성자 및 비밀번호를 포함해야 한다
- 작성자 정보 수정
  - SalesItem 과 동일 ( 방법 2 채택 )
- 추가적으로 진행할 사항
  - validation 진행
  - 현재 물품이 존재하는지 물품 ID 를 통해 직접 SalesItemTable 에 query 를 날려 검증, 또한 물품에 해당하는 댓글인지, 댓글의 작성자가 맞는지 전부 댓글 ID 를 기반으로 조회한 후 검증
    - 조회를 할 때 물품 ID, 댓글 ID, 작성자 정보를 모두 만족하는 레코드를 조회하도록 수정 가능
    - 기존 방식은 물품이 없는지, 댓글이 없는지, 작성자 정보가 틀린 것인지 세세하게 예외처리 가능
    - 고민중인 방식은 세세한 예외처리는 불가능 하지만 query 수가 줄고 레코드를 조회할 범위가 줄어드는 장점이 있음
  - 답글 삭제 기능

### 제안
기능 | HTTP METHOD | URL
 --- | ----------- | ---
 제안 등록 | POST | /items/{itemId}/proposals
 제안 전체 조회 | GET | /items/{itemId}/proposals?writer={writer}&password={password}&page={page}&limit={limit}
 제안 삭제 | DELETE | /items/{itmeId}/proposals/{proposalId}
 제안 수정, 수락, 거절, 구매 확정 | PUT | /items/{itemId}/proposals/{proposalId}
 
#### NegotiationDto
제안에 관한 사용자 요청과 응답 표현을 위한 DTO, 요구 사항 마다 Request 와 Response 가 달라 inner class 를 활용

#### ResponseDto, PageDto, UserDto 재사용

#### 등록, 조회, 수정, 삭제, 제안 수락 / 거절 / 구매 확정
- 등록
  - 물품 ID 를 기반으로 제안 등록, 작성자 / 비밀번호 / 제안 가격은 필수 사항
  - 물품 ID 기반으로 해당 물품이 존재하는지 확인
- 조회
  - 물품 ID 를 기반으로 해당 물품이 존재하는지 확인 
  - 물품 ID 와 작성자 정보를 기반으로 물품 등록자의 조회 요청인지 제안 등록자의 조회 요청인지 분류
  - 물품 등록자의 경우 해당 물품 ID 기반으로 전체 제안 조회 ( 페이지 단위 조회 )
  - 제안 등록자의 경우 해당 물품 ID 와 작성자 정보 기반으로 제안 작성자의 제안만 조회 ( 페이지 단위 조회 )
- 제안 수정, 수락, 거절, 구매 확정
  - Validator 와 Custom Annotation 을 활용해 status 값이 {수락, 거절, 확정} 만 가능하도록 유효성 검사
  - 물품 ID 기반으로 해당 물품이 존재하는지 확인
  - requestBody 의 status 와 suggestPrice 항목의 null 여부를 체크해 제안의 가격 수정 요청인지 제안 수락, 거절, 구매확정 요청인지 분류
  - 가격 수정 요청의 경우
    - 제안 ID, 작성자 정보를 기반으로 제안이 존재하는지, 작성자 정보가 일치하는지 확인
    - 물품 ID 와 제안 ID 를 기반으로 제안 조회
  - 가격 수정 요청이 아닌 경우
    - 해당 요청의 RequestBody 의 Status 가 "확정" 인지 확인
    - "확정"이 아니라면 "수락" 혹은 "거절" --> 물품 등록자의 요청
      - 물품 ID 와 작성자 정보를 기반으로 작성자 정보가 일치하는지 확인
      - 물품 ID 와 제안 ID 를 기반으로 제안 조회
      - 해당 제안이 "제안" 상태이고 아직 해당 물품에 "수락" 상태인 제안이 없는지 확인 ( 구매 확정이 이루어지면 확정된 제안은 "확정" 상태가 되고 그 외에는 "거절" 상태가 되기에 "수락" 상태가 있는지만 확인 )
    - "확정"이라면 구매확정을 위한 요청 --> 제안 등록자의 요청
      - 제안 ID 와 작성자 정보를 기반으로 작성자 정보가 일치하는지 확인
      - 물품 ID 와 제안 ID 를 기반으로 제안 조회
      - 해당 제안의 상태가 "수락" 상태면 "확정" 상태로 변경, 그외 제안 "거절" 상태로 변경 및 물품의 상태를 "판매 완료" 로 변경
- 제안 삭제
  - 물품 ID 를 기반으로 해당 물품이 존재하는지 확인
  - 제안 ID 와 작성자 정보를 기반으로 작성자 정보가 일치하는지 확인
  - 물품 ID 와 제안 ID 기반으로 제안 조회
- 추가적으로 진행할 사항
  - 위의 내용과 동일
  - 작성자 정보 수정 추가
  - 제안 등록, 수정, 삭제 기능을 수행하기 전 물품의 상태가 "판매 완료" 상태인지 확인
  - update 에 관련된 service 를 모두 한 url 에 mapping 을 진행하기 위해 RequestBody 의 status 와 suggestPrice 의 값의 유무를 확인해 조건 분기를 진행하였는데,
    이 부분은 두 값 모두 존재하는 경우 문제가 생길 수 있다. 이 부분에 대한 해결이 필요 ( mapping url을 분리하는 것이 좋다고 판단 )
  - 각 예외를 HttpStatus Code 를 이용해 처리하고 사용자에게 표현을 하고 있는데 이 부분 또한 처리가 필요

## 추가 정보
resource 디렉토리에 각 table 을 정의한 sql 파일이 있습니다. SalesItem Table 의 기본 Test Data 를 정의해 놓았습니다.
[Application.yaml & Postman Collection](https://www.notion.so/Spring-13646b407a5140c6b05c86400d94b63a)



 

