# Todo API

## Skill
![Java](https://img.shields.io/badge/Java-21-007396?style=flat-square&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.5-6DB33F?style=flat-square&logo=spring-boot&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-6DB33F?style=flat-square&logo=spring&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6DB33F?style=flat-square&logo=springsecurity&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-000000?style=flat-square&logo=jsonwebtokens&logoColor=white)
![OAuth2](https://img.shields.io/badge/OAuth2-000000?style=flat-square&logo=oauth&logoColor=white)
![SQLite](https://img.shields.io/badge/SQLite-003B57?style=flat-square&logo=sqlite&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=flat-square&logo=swagger&logoColor=black)
![JUnit5](https://img.shields.io/badge/JUnit5-25A162?style=flat-square&logo=junit5&logoColor=white)


## Features
- 사용자 로그인, 회원가입, 조회, 수정, 삭제
  - 수정 시 수정 필드는 모두 옵션. e.g. nickname 만 수정 또는 password만 수정 가능
- Todo 를 생성, 조회, 수정, 삭제, 검색
  - 작성자에 해당되는 Todo만 조회, 수정, 삭제, 검색
  - 수정 시 수정 필드는 모두 옵션. e.g. title 만 수정 또는 complete만 수정 가능
- JWT 기반 사용자 인증
- OAuth2 Google 제공
  - 확장 가능하도록 provider 정보 관리
  - 
## 실행 방법
```shell
./gradlew bootRun
```

## Swagger

- URL : http://localhost:8080/swagger-ui/index.html


## API 명세

### 인증 API

#### 회원가입
- **POST** `/users/signup`
- **Request Body**
  ```json
  {
    "email": "string",
    "nickname": "string",
    "password": "string"
  }
  ```
- **Response**: 201 Created
  ```json
  {
    "data": {
      "email": "string",
      "nickname": "string"
    }
  }
  ```

#### 로그인
- **POST** `/users/login`
- **Request Body**
  ```json
  {
    "email": "string",
    "password": "string"
  }
  ```
- **Response**: 200 OK
  ```json
  {
    "data": {
      "accessToken": "string"
    }
  }
  ```

### 사용자 API

#### 내 정보 조회
- **GET** `/users/me`
- **Headers**: `Authorization: Bearer {token}`
- **Response**: 200 OK
  ```json
  {
    "data": {
      "email": "string",
      "nickname": "string"
    }
  }
  ```

#### 내 정보 수정
- **PUT** `/users/me`
- **Headers**: `Authorization: Bearer {token}`
- **Request Body**
  ```json
  {
    "nickname": "string"
  }
  ```
- **Response**: 200 OK
  ```json
  {
    "data": {
      "email": "string",
      "nickname": "string"
    }
  }
  ```

#### 내 정보 삭제
- **DELETE** `/users/me`
- **Headers**: `Authorization: Bearer {token}`
- **Response**: 204 No Content

### 할 일 API

#### 할 일 생성
- **POST** `/todos`
- **Headers**: `Authorization: Bearer {token}`
- **Request Body**
  ```json
  {
    "title": "string",
    "description": "string"
  }
  ```
- **Response**: 201 Created
  ```json
  {
    "data": {
      "id": "number",
      "title": "string",
      "description": "string",
      "completed": "boolean"
    }
  }
  ```

#### 할 일 목록 조회
- **GET** `/todos`
- **Headers**: `Authorization: Bearer {token}`
- **Response**: 200 OK
  ```json
  {
    "data": [
      {
        "id": "number",
        "title": "string",
        "description": "string",
        "completed": "boolean"
      }
    ]
  }
  ```

#### 할 일 상세 조회
- **GET** `/todos/{id}`
- **Headers**: `Authorization: Bearer {token}`
- **Response**: 200 OK
  ```json
  {
    "data": {
      "id": "number",
      "title": "string",
      "description": "string",
      "completed": "boolean"
    }
  }
  ```

#### 할 일 수정
- **PUT** `/todos/{id}`
- **Headers**: `Authorization: Bearer {token}`
- **Request Body**
  ```json
  {
    "title": "string",
    "description": "string",
    "completed": "boolean"
  }
  ```
- **Response**: 200 OK
  ```json
  {
    "data": {
      "id": "number",
      "title": "string",
      "description": "string",
      "completed": "boolean"
    }
  }
  ```

#### 할 일 삭제
- **DELETE** `/todos/{id}`
- **Headers**: `Authorization: Bearer {token}`
- **Response**: 204 No Content

#### 할 일 검색
- **GET** `/todos/search?title={검색어}`
- **Headers**: `Authorization: Bearer {token}`
- **Response**: 200 OK
  ```json
  {
    "data": [
      {
        "id": "number",
        "title": "string",
        "description": "string",
        "completed": "boolean"
      }
    ]
  }
  ```

