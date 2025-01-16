# POS 정산 시스템 
### 프로젝트 기획 문서 🗂️ → [링크](https://yuseogi0218.notion.site/POS-1615a0fb769580e39a1efa20872be862)
<img width="711" alt="image" src="https://github.com/user-attachments/assets/1bb8ade1-0c61-473b-9ce9-91119aaa6044" />

### 음식점을 대상으로 한 POS(Point Of Sale) 시스템 및 정산 시스템
- 상점 주인과 고객을 위한 상품 주문 및 거래 완료(가상 결제) 기능 제공 
- 상점 주인을 위한 상품 판매 통계 및 거래 정산을 위한 **배치 프로그램 및 확장성을 고려한 설계 적용**
---

**👨‍💻1인 프로젝트**

**프로젝트 수행 기간** : 2024.12.18 ~ 2025.01.18

**Backend**

![Java](https://img.shields.io/badge/-Java-333333?style=flat&logo=Java)
![springboot](https://img.shields.io/badge/-springboot-333333?style=flat&logo=springboot)
![springdatajpa](https://img.shields.io/badge/-springdataJPA-333333?style=flat&logo=spring)
![springcloud](https://img.shields.io/badge/-springcloud-333333?style=flat&logo=iCloud&logoColor=6DB33F)
![springbatch](https://img.shields.io/badge/-springbatch-333333?style=flat&logo=Buffer&logoColor=6DB33F)
![springsecurity](https://img.shields.io/badge/-springsecurity-333333?style=flat&logo=springsecurity)
![JWT](https://img.shields.io/badge/-JWT-333333?style=flat&logo=jsonwebtokens)
![MySQL](https://img.shields.io/badge/-MySQL-333333?style=flat&logo=mysql)
![Redis](https://img.shields.io/badge/-Redis-333333?style=flat&logo=Redis)
![Kafka](https://img.shields.io/badge/-Kafka-333333?style=flat&logo=apachekafka)

**DevOps & Testing**

![Git](https://img.shields.io/badge/-Git-333333?style=flat&logo=git)
![GitHub](https://img.shields.io/badge/-GitHub-333333?style=flat&logo=github)
![Docker](https://img.shields.io/badge/-Docker-333333?style=flat&logo=docker)
![Pushgateway](https://img.shields.io/badge/-Pushgateway-333333?style=flat&logo=prometheus&logoColor=2480E6)
![Prometheus](https://img.shields.io/badge/-Prometheus-333333?style=flat&logo=prometheus)
![Grafana](https://img.shields.io/badge/-Grafana-333333?style=flat&logo=grafana)
![Postman](https://img.shields.io/badge/-Postman-333333?style=flat&logo=postman)
![AWS-EC2](https://img.shields.io/badge/-AmazonEC2-333333?style=flat&logo=AmazonEC2)
![AWS-EC2](https://img.shields.io/badge/-AmazonRDS-333333?style=flat&logo=AmazonRDS)

---

## 주요 비즈니스 구현 기능
- User Domain
  - 상점 주인 소셜(카카오) 회원가입 및 로그인 / 로그아웃
- Store Domain
  - 상점 주인에 의한 판매 상품 관리(CRUD)
  - 상품 판매 통계 및 정산 데이터 조회
- Trade Domain
  - 주문용 태블릿 기기에 의한 상품 주문
  - 현재 진행중인 거래의 주문 내역 조회
  - 상점 주인에 의한 현재 진행중인 거래의 주문 내역에 대한 일괄 (현금/카드) 결제
- Batch Server
  - 1일, 1주(월 ~ 일), 1달 단위의 상품 판매 통계 데이터 생성
  - 1일, 1달(상점별 정산일)의 정산 데이터 생성

---
## 프로젝트 기술적 경험
> **프로젝트 테스트 실행 환경**
> - 서버 자원 - AWS EC2 → t2.micro 인스턴스 (Free-Tier), 가상 CPU 1 core, 메모리 1GiB
> - 데이터베이스 자원 - AWS RDS → db.t4g.micro 인스턴스 (Free-Tier), 가상 CPU 2 core, 메모리 1GiB
> 
> *보통 vCPU(가상 CPU) 는 물리 CPU 사양의 절반 정도의 성능을 가진다.*
### 1. 약 280만건의 거래 데이터에 대해서 1000건 결과 데이터 집계 시, 30초 이내로 배치 프로그램 수행 완료
- 약 100만건의 거래 데이터에 대해서 100건 결과 데이터 집계 시, 5초 이내로 배치 프로그램 수행 완료
- Spring Batch 를 활용한 배치 프로그램 작성
  - Chunk 기반으로 JdbcPagingItemReader 와 JdbcBatchItemWriter 를 활용한 배치 프로그램 구성  
- GROUP BY + SUM 쿼리를 활용한 집계 수행
> 예를 들어 50만개의 상품에 대한 판매 데이터 1,000만개가 있다고 하자. 이때 집계 결과로 50만개의 상품 판매 통계 데이터가 생성되게 된다.

![Architecture](https://github.com/user-attachments/assets/5eb9c3fd-7e20-44e0-9930-fba6973f36be)


### 2. 서비스 규모 증가 및 확장성을 고려한 배치 프로그램 설계 적용 → [자세히 보기](https://yuseogi0218.notion.site/Batch-Server-17a5a0fb769580e8b291c483422351d9)
![New Architecture](https://github.com/user-attachments/assets/e75c6491-e747-4258-aadf-9a7c8d9e30d4)

- Database에서 GROUP BY + SUM 쿼리를 통해서 수행하던 집계 역할을, Redis 가 대신 수행하는 설계
  - 쿼리 작성 및 유지보수에 대한 리소스 절약과 쿼리 실행 계획의 단순화를 통해 대규모 데이터 처리 시, 성능 향상 기대
  - Redis 의 O(1) 의 시간복잡도를 보장하는 hash 함수와 다수의 연산을 묶어서 Redis 에게 요청하는 Redis Pipeline 을 통한 성능 저하 방지


- 약 280만건의 거래 데이터에 대해서 1000건 결과 데이터 집계 시, 4분 20초 이내로 배치 프로그램 수행 완료
- 약 100만건의 거래 데이터에 대해서 100건 결과 데이터 집계 시, 45초 이내로 배치 프로그램 수행 완료
  - 기존 18분 이내로 수행되던 작업 → 내장 함수 제거와 인덱스 추가등의 쿼리 튜닝을 통해 45초로 단축 **(95.94% 성능 개선)**

### ⭐️ 프로젝트 Insight 💫
Ver 1 : GROUP BY + SUM 쿼리를 활용한 집계 수행, Ver 2 : Redis 를 통한 집계 수행
- 데이터가 작고 쿼리가 단순한 서비스의 초기 단계일 경우에는, Ver 1이 더 적합함
  - 하지만 Ver 1 의 GROUP BY + SUM과 같은 쿼리는 연산이 데이터베이스에 의존적이며 성능 저하 및 난해한 쿼리 튜닝의 원인이 될 수 있음


- 서비스의 도메인(데이터베이스 스키마) 증가 및 데이터 누적으로 인해 서비스의 규모가 커지면, Ver 2가 더 적합한 방법
  - 데이터량이 증가하고 쿼리가 복잡해질수록 Ver 2가 시간 및 리소스 절약 측면에서 유리함
  - 또한, 쿼리가 단순해져서 코드 작성 및 유지보수도 더욱 간결해질 수 있음

### 3. Prometheus With Pushgateway & Grafana 를 통한 배치 프로그램 모니터링 구성
- Batch 와 같은 단발성 작업으로 설계된 Task의 지표를 Prometheus 에 역으로 Push 하기 위해서 Pushgateway를 사용함
  - Prometheus 는 기본적으로 매트릭 지표를 제공하는 서버에게 주기적으로 요청(pull)하여 매트릭을 수집하도록 되어있음
- 배치 프로그램의 성능 분석을 위한 자체적으로 Grafana Dashboard 구성
- Application Grafana Dashboard 와 결합

**배치 프로그램 모니터링**
- 전체 Job 실행 횟수, Job 성공 횟수, Job 실패 횟수, Job 실행 시간 등의 지표를 확인할 수 있음

![batch-server](https://github.com/user-attachments/assets/bfce70cc-ad92-4006-95b6-e5f82bb06dcd)

**Application 모니터링**
- CPU, JVM Heap 상태, 각 API 별 초당 요청 횟수 및 각 API 별 응답 시간을 확인할 수 있음

![trade-service](https://github.com/user-attachments/assets/28d3ddfe-b1dc-4a45-be14-396b2e6de775)

### 4. 프로젝트 문서화 진행 → [링크](https://yuseogi0218.notion.site/hanghae99-project)
- 프로젝트 진행 시, 내용 정리 및 진행사항 트래킹을 위해 문서화 작업 수행
- 항해99 취업 리부트 코스 6기 - 우수 프로젝트 기록 선정

---

## 마이크로 서비스 아키텍처 ⚙️
> 이미지 클릭 시, 확대하여 자세히 보실 수 있습니다.

![Service Architecture](https://github.com/user-attachments/assets/d9b71c0c-35da-4237-8d3a-8a0a02e557f7)

---

## ERD 🪣
> 이미지 클릭 시, 확대하여 자세히 보실 수 있습니다.

![ERD](https://github.com/user-attachments/assets/db3acfb8-2c9c-432f-81a1-5614ab57b4d2)

---

- ### 기능 명세서 📋 → [링크](https://yuseogi0218.notion.site/1605a0fb7695810c8b2cc0581a52eef4)
- ### API 문서 🛰️ → [링크](https://yuseogi0218.notion.site/API-1605a0fb769581be8a15d55ab6dd59a4)
- ### Error Code 🚫 → [링크](https://yuseogi0218.notion.site/Error-Code-1605a0fb7695813bb382d3e1ba8b20e8)
- ### 기술적 의사 결정 ⚒️ → [링크](https://yuseogi0218.notion.site/17b5a0fb76958069a9fde8646ebeb35b)

---

## 프로젝트 실행 방법
1. Github Repository Clone
2. Kakao 개발자 센터 App 등록 및 설정 For OAuth Login 
3. .env 파일 생성 및 아래 항목 설정
    ```properties
    MYSQL_ROOT_USERNAME={username}
    MYSQL_ROOT_PASSWORD={password}
    
    JWT_SECRET_KEY={secret key}
    
    KAKAO_CLIENT_ID={kakao_client_id}
    KAKAO_ACCOUNT_URI="https://kapi.kakao.com/v2/user/me"
    KAKAO_REDIRECT_URI="http://localhost:8000/user/page/signup?oauth=kakao"
    ``` 
4. docker compose file 실행
    ```shell
    docker compose -f docker-compose.infrastructure.yml -p pos up -d
    docker compose -f docker-compose.application.yml -p pos up -d
    docker compose -f docker-compose.monitoring.yml -p pos up -d
    ```
5. database 접속 및 schema.sql 의 DDL 실행
    - service-db (:3307)
      - [user-domain](user-service/src/main/resources/database/schema.sql)
      - [store-domain](store-service/src/main/resources/database/schema.sql)
      - [trade-domain](trade-service/src/main/resources/database/schema.sql)
      - [batch-server](batch-server/src/main/resources/database/schema.sql)
    - meta-db (:3308)
      - [batch-server](batch-server/src/main/resources/database/meta-schema.sql)
---

**프로젝트 참고**
> - [[우아한 형제들] 정산 시스템 팀을 소개합니다.](https://techblog.woowahan.com/2701/)
> - [[TossPayments] API 문서 - 정산](https://docs.tosspayments.com/reference#%EC%A0%95%EC%82%B0)
> - [[if kakao 2022] Batch Performance를 고려한 최선의 Aggregation](https://tech.kakaopay.com/post/ifkakao2022-batch-performance-aggregation/)
