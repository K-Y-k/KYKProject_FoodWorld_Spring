# :rocket: FoodWorld_Project
지금까지 배운 스프링 MVC와 DB, JPA와 queryDsl을 활용한 CRUD 개인 프로젝트: 음식 커뮤니티 웹 사이트 리뉴얼

- 개발 인원: 1명

- 개발 기간: 2023.01 ~ 2023.04


# :page_facing_up: 목차
- [어떤 프로젝트인가요?\(프로젝트의 목적\)](#어떤-프로젝트인가요?(프로젝트의-목적))
- [개발 환경](#개발-환경)
- [사용 기술](#사용-기술)
   * [FrontEnd](#frontend)
   * [BackEnd](#backend)
   * [기타 주요 라이브러리](#기타-주요-라이브러리)
- [E-R 다이어그램](#e-r-다이어그램)
- [아키텍처 구성](#아키텍처-구성)
- [구현 영상](#구현-영상)
- [핵심 기능](#핵심-기능)
- [어려웠던 점 및 극복](#어려웠던-점-및-극복)
- [프로젝트를 통해 느낀점](#프로젝트를-통해-느낀점)
- [프로젝트 관련 추가 포스팅](#프로젝트-관련-추가-포스팅)


## :question: 어떤 프로젝트인가요?\(프로젝트의 목적\)
백엔드 웹 개발자가 되기 위해 Jdbc, DB, Servlet의 기초를 다지고 

Spring과 관련된 Spring 기초, HTTP 네트워크, MVC 패턴, DB 연결, JPA, SpringDataJPA, queryDsl을 학습한 후 배운 내용을 나의 것으로 만들기 위해 처음 구현해본 프로젝트입니다.

사람에게 중요한 것중의 하나인 음식을 타깃으로 선정하고 공감대를 형성하기 좋은 음식의 주제로 소통을 할 수 있고 음식 메뉴를 선정할 때 도움을 제공하는 서비스로

기본적으로 게시글/댓글을 작성할 수 있고 추가로 다른 회원과 실시간 채팅을 나눌 수 있고 회원들이 등록한 메뉴를 랜덤으로 추첨할 수 있습니다.


## :office: 개발 환경
- ### IDE
  * IntelliJ IDEA Community Edition 2022
  * Visual Studio Code 1.7.8
  * SQL Develpoer
  * Postman
  * GitHub

## 🛠 사용 기술
### FrontEnd
- #### 언어 / 프레임워크 / 라이브러리
  * Html/Css
  * Javascript
  * Bootstrap 5.2
  * Jquery
  * Thymeleaf

### BackEnd
- #### 언어 / 프레임워크 / 라이브러리
  * Java 11 openjdk
  * SpringBoot 2.7.7
  * Jpa(Spring Data JPA)
  * QueyDsl

- #### Build tool
  * Gradle 7.6

- #### Server
  * Apache Tomcat 9.0
  
- #### Database
  * Oracle Database 18c Express Edition

### 기타 주요 라이브러리
- Lombok
- websoket


## :key: E-R 다이어그램
<img src="https://user-images.githubusercontent.com/102020649/236933959-3cf3eab5-5a8d-4c73-8366-788c5c452221.png" width="800" height="500"/>


## :mag: 아키텍처 구성
<img src="https://user-images.githubusercontent.com/102020649/236934201-a2786334-eb1a-42cf-ab9f-49f51039514e.png" width="800" height="500"/>


## :arrow_forward: 구현 영상
[![Video Label](http://img.youtube.com/vi/uZDP6DqmpI8/0.jpg)](https://youtu.be/uZDP6DqmpI8)


## :heavy_check_mark: 주요 기능


## :fire: 어려웠던 점 및 극복
### 1. Slice 페이징의 한계
- Slice 페이징은 현재 받아온 엔티티의 id보다 <(작은 것)부터 가져와야 하는데 그렇게 되면 첫 페이지의 첫 데이터를 가져오지 못합니다. 그렇다고 <=(작거나 같음)으로 설정하면 끝의 데이터 후 다음 페이지에서 끝의 데이터가 한번 또 나오게 됩니다.
- 제가 생각한 방안은 첫 페이지인지의 여부를 파라미터로 설정해서 각 상황에 따른 첫 페이지 파라미터를 갱신해가며, 첫 페이지일 때는 <=, 첫 페이지가 아니면 <으로 모든 데이터를 가져올 수 있게 하였습니다.

### 2. JSON 양방향으로 매핑된 엔티티 필드의 무한 참조 발생
- Ajax로 API를 호출할 때 해당 조회하는 엔티티에 양방향으로 매핑된 엔티티가 있으면 서로 참조가 되어 무한 참조 문제가 발생합니다.

- 먹스타그램의 게시글 엔티티를 불러올 때는 회원 엔티티 필드의 정보가 필요가 없어 회원 엔티티를 @JsonIgnore로 간단히 방지할 수 있었지만, 회원 엔티티를 불러오는 관리자 페이지에서는 회원과 양방향으로 매핑된 게시글, 프로필, 메뉴, 채팅방 엔티티들의 정보가 필요하여 JSON을 호출할 때 DTO를 활용하여 필요한 API 스펙으로 변경하고 호출하였습니다.

### 3. 1 : N 관계에서의 leftJoin으로 중복 레코드 발생 
- 1 : 1, N : 1 관계에서는 leftJoin으로 row의 수가 틀어지지 않는데, 1 : N 관계에서는 중복 레코드가 발생하였습니다. 
- 1 : N 관계처럼 하나의 키에 여러 데이터가 있으면 그 데이터 수만큼의 row가 생겨 중복 레코드가 발생한 것입니다. 이때는 Distinct를 활용해 중복 레코드를 제거하였습니다.

### 4. QueryDsl 랜덤 선택의 한계
- QueryDsl에서는 랜덤으로 select 하는 기능이 지원하지 않습니다. 
- 그래서 네이티브 SQL로 작성을 시도해봤지만 잘 되지 않아 대안책으로 QueryDsl로 관련 리스트를 가져오고 Java의 Random() 함수를 활용하여 해당 리스트 중에서 랜덤으로 선택하게 하였습니다.

### 5. 새로운 기술 습득 및 극복
- 처음으로 접해보는 기술로 실시간 채팅에서 사용되는 Websocket, Stomp의 원리를 이해는 했지만 서핑하여 얻은 코드의 정보가 이해가 가지 않았습니다. 해당 코드 정보는 실시간 채팅이 주였고 저는 DB에 채팅 내역을 저장 및 출력까지 구현해야했기에 사이클을 명확히 이해했어야 했습니다.
- 하지만 계속 코드를 반복적으로 리뷰하고 소켓 연결 후 채팅방 생성, 사용자 입장/대화/퇴장(소켓 끊기)의 경우에 따른 사이클 흐름을 따라가는 과정을 통해 코드가 읽혀짐으로써 제가 구현하고 싶었던 프로젝트에 맞춰 적용시켰습니다. 


## :bulb: 프로젝트를 통해 느낀점
- 혼자서 백엔드/프론트 모든 과정을 거치면서 개발의 전체적인 라이프 사이클을 파악할 수 있었음
- 기본적인 엔티티 설계 및 관계 매핑, 리포지토리/서비스/컨트롤러/뷰의 계층과 MVC 구조에 적응하였고 쿼리 파라미터의 전송, 전달에 능숙해짐
- 기능을 구현할 때 발생할 수 있는 모든 테스트 케이스들을 면밀히 분석해야함
- 기능을 구현할 때 필요한 쿼리 파악과 쿼리를 구현하기 위한 JPQL, SpringDataJpa, QueryDsl 기술중 적절한 선택 및 구현이 가능해짐
- 구현 중 막히는 상황이 많았지만 그런 상황속에도 포기하지 않고 끝까지 몰두하는 과정을 통해 문제해결 능력을 기를 수 있었음 


## :ledger: 프로젝트 관련 추가 포스팅
- [프로젝트 명세서](https://blog.naver.com/kyk7777_/222975254105)
- [구조 설계 / 오라클 서버 세팅 / 스프링 세팅](https://blog.naver.com/kyk7777_/222975254105)
- [회원 가입 / 로그인, 로그아웃 / 유효성 검사 / 회원 탈퇴](https://blog.naver.com/kyk7777_/222978032496)
- [게시판 CRUD / 조회수 증가](https://blog.naver.com/kyk7777_/222979729796)
- [페이징 / 검색](https://blog.naver.com/kyk7777_/222980904399)
- [좋아요](https://blog.naver.com/kyk7777_/222988457441)
- [댓글 CRUD / 페이징](https://blog.naver.com/kyk7777_/222989407593)
- [파일 업로드](https://blog.naver.com/kyk7777_/222992202791)
- [파일 다운로드](https://blog.naver.com/kyk7777_/222993172577)
- [먹스타그램 무한 스크롤 페이징 / 검색 / 좋아요 (Ajax와 JSON 활용)](https://blog.naver.com/kyk7777_/223028739381)
- [먹스타그램 무한 스크롤 문제 및 해결](https://blog.naver.com/kyk7777_/223032104975)
- [프로필 조회 / 수정](https://blog.naver.com/kyk7777_/223033274407)
- [팔로우/언팔로우, 팔로워 리스트, 팔로워 연관 회원 추천 리스트](https://blog.naver.com/kyk7777_/223035839069)
- [소켓 통신 1 대 1 채팅방](https://blog.naver.com/kyk7777_/223039140976)
- [메뉴 랜덤 추첨](https://blog.naver.com/kyk7777_/223058461117)
- [메인 페이지, 인기글](https://blog.naver.com/kyk7777_/223067248635)
- [추천 게시판 카테고리별 필터링 조회](https://blog.naver.com/kyk7777_/223075193623)
- [Admin 페이지](https://blog.naver.com/kyk7777_/223089039632)
