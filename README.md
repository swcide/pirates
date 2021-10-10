# pirates

더파이러츠 백엔드 개발자 채용 과제

## 1. 설치 및 환경설정 가이드

1. 기술 : Spring-boot, Spring-data-jpa, QueryDsl, Lombook,H2
2. build.gradle

    ```java
    plugins {
        id 'org.springframework.boot' version '2.5.5'
        id 'io.spring.dependency-management' version '1.0.11.RELEASE'
        id 'java'
    }

    group = 'com'
    version = '0.0.1-SNAPSHOT'
    sourceCompatibility = '1.8'

    configurations {
        compileOnly {
            extendsFrom annotationProcessor
        }
    }

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
        implementation 'org.springframework.boot:spring-boot-starter-web'
        compileOnly 'org.projectlombok:lombok'
        runtimeOnly 'com.h2database:h2'
        annotationProcessor 'org.projectlombok:lombok'
        testImplementation 'org.springframework.boot:spring-boot-starter-test'

        // https://mvnrepository.com/artifact/com.googlecode.json-simple/json-simple
        implementation group: 'com.googlecode.json-simple', name: 'json-simple', version: '1.1.1'

        // queryDsl
        implementation 'com.querydsl:querydsl-jpa'

        annotationProcessor 'org.projectlombok:lombok'
        annotationProcessor "com.querydsl:querydsl-apt:${dependencyManagement.importedProperties['querydsl.version']}:jpa"
        annotationProcessor("jakarta.persistence:jakarta.persistence-api")
        annotationProcessor("jakarta.annotation:jakarta.annotation-api")
    }

    def generated= "$buildDir/generated/querydsl"
    sourceSets {
        main.java.srcDirs += [ generated ]
    }
    tasks.withType(JavaCompile) {
        options.annotationProcessorGeneratedSourcesDirectory = file(generated)
    }

    clean.doLast {
        file(generated).deleteDir()
    }

     test {
        useJUnitPlatform()
    }
    ```

## 2. 테이블 생성 SQL

1. STORE 테이블

    ```sql
    CREATE TABLE IF NOT EXISTS STORE(
    		STORE_ID BIGINT auto_increment primary key, 
    		CREATED_AT TIMESTAMP, 
    		MODIFIED_AT TIMESTAMP,
    		CLOSING TIME,
    		TYPE VARCHAR(255),
    		DESCRIPTION TEXT,
    		NAME  VARCHAR(255)
    );
    ```

2. OPTION 테이블

    ```sql
    CREATE TABLE IF NOT EXISTS OPTIONS (
    		OPTION_ID BIGINT auto_increment PRIMARY KEY, 
    		CREATED_AT TIMESTAMP, 
    		MODIFIED_AT TIMESTAMP,
    		NAME  VARCHAR(255),
    		PRICE BIGINT,
    		STOCK BIGINT,
    		STORE_ID BIGINT
    );
    ```

3. HOLIDAY 테이블

    ```sql
    CREATE TABLE IF NOT EXISTS HOLIDAY(
    		ID BIGINT PRIMARY KEY, 
    		DATE DATE, 
    		DATE_NAME VARCHAR(255)
    		TYPE VARCHAR(255)
    );
    ```

## 3 . API 사용 가이드

<details>
   <summary>점포 추가 API</summary>
   
   |기능|Method|URL|
   |------|---|---|
   |점포 추가|POST|/api/stores|

- Request
```json
{
	"name": "노르웨이산 연어",
	"description": "노르웨이산 연어 300g, 500g, 반마리 필렛",
	"delivery": {
            "type": "fast",
            "closing": "12:00"
        },
        "options": [
            {
               "name": "생연어 몸통살 300g",
               "price": 10000,
               "stock": 99
            },
            {
               "name": "생연어 몸통살 500g",
               "price": 20000,
               "stock": 99
            }
        ]
}
```
- Response
```json
  1
```


</details>

<details>
   <summary>상품 목록 조회 API</summary>

   |기능|Method|URL|
   |------|---|---|
   |상품 목록 조회|GET|/api/stores|

- Request

- Response
```json
  [
   {
      "name": "완도전복",
      "description": "산지직송 완도 전복 1kg (7미~60미)",
      "price": "10,000 ~"
   },
   {
      "name": "노르웨이산 연어",
      "description": "노르웨이산 연어 300g, 500g, 반마리 필렛",
      "price": "10,000 ~"
   }
]
```


</details>
<details>
   <summary>상품 상세 조회 API</summary>

   |기능|Method|URL|
   |------|----|-----|
   |상품 상세 조회|GET|/api/stores/{id}|

- Request
- Response
```json
 {
   "name": "노르웨이산 연어",
   "description": "노르웨이산 연어 300g, 500g, 반마리 필렛",
   "delivery": "fast",
   "options": [
      {
         "name": "생연어 몸통살 300g",
         "price": 10000
      },
      {
         "name": "생연어 몸통살 500g",
         "price": 20000
      }
   ]
}
```


</details>
<details>
   <summary>수령일 선택 목록 API</summary>

   |기능|Method|URL|
   |------|---|---|
   |수령일 선택 목록|GET|/api/stores/arrives/{id}|

PDF상의 응답일은 화요일 부터 시작으로 되어있지만 월요일이 공휴일 이므로 수요일부터 수령이 가능한 것으로 생각 되어 수정하였습니다.
- Request


- Response
```json
[
   {
      "date": "10월 13일 수요일"
   },
   {
      "date": "10월 14일 목요일"
   },
   {
      "date": "10월 15일 금요일"
   },
   {
      "date": "10월 16일 토요일"
   },
   {
      "date": "10월 18일 월요일"
   }
]

```


</details>
<details>
   <summary> 점포 삭제 API</summary>

   |기능|Method|URL|
   |------|---|---|
   |점포 삭제|DELETE|/api/stores/{id}|


</details>
