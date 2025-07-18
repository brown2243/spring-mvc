# 스프링 MVC 1편 - 백엔드 웹 개발 핵심 기술

## 2. 웹 애플리케이션 이해

### 5. 웹 서버, 웹 애플리케이션 서버

- 웹 서버(Web Server)

  - HTTP 기반으로 동작
  - 정적 리소스 제공, 기타 부가기능
  - 정적(파일) HTML, CSS, JS, 이미지, 영상
  - 예) NGINX, APACHE

- 웹 애플리케이션 서버(WAS - Web Application Server)

  - HTTP 기반으로 동작
  - 웹 서버 기능 포함+ (정적 리소스 제공 가능)
  - 프로그램 코드를 실행해서 애플리케이션 로직 수행
  - 동적 HTML, HTTP API(JSON)
  - 서블릿, JSP, 스프링 MVC
  - 예) 톰캣(Tomcat) Jetty, Undertow

- 웹 시스템 구성 - WAS, DB

  - WAS, DB 만으로 시스템 구성 가능
  - WAS는 정적 리소스, 애플리케이션 로직 모두 제공 가능
  - WAS가 너무 많은 역할을 담당, 서버 과부하 우려
  - 가장 비싼 애플리케이션 로직이 정적 리소스 때문에 수행이 어려울 수 있음
  - WAS 장애시 오류 화면도 노출 불가능

- 웹 시스템 구성 - WEB, WAS, DB

  - 정적 리소스는 웹 서버가 처리
  - WAS는 중요한 애플리케이션 로직 처리 전담
  - 웹 서버는 애플리케이션 로직같은 동적인 처리가 필요하면 WAS에 요청을 위임
  - 효율적인 리소스 관리
    - 정적 리소스가 많이 사용되면 Web 서버 증설
    - 애플리케이션 리소스가 많이 사용되면 WAS 증설
  - WAS, DB 장애시 WEB 서버가 오류 화면 제공 가능

- API server로 was라면 웹서버는 굳이 필요없다.

### 6. 서블릿

- 서버에서 처리해야 하는 업무를 웹 애플리케이션 서버 직접 구현해야 한다면 아래 전체 구현 필요
  - 서버 TCP/IP 연결 대기, 소켓 연결
  - HTTP 요청 메시지를 파싱해서 읽기
  - POST 방식, /save URL 인지
  - Content-Type 확인
  - HTTP 메시지 바디 내용 피싱
  - username, age 데이터를 사용할 수 있게 파싱
  - 저장 프로세스 실행
  - 비즈니스 로직 실행
  - 데이터베이스에 저장 요청
  - HTTP 응답 메시지 생성 시작
  - HTTP 시작 라인 생성
  - Header 생성
  - 메시지 바디에 HTML 생성에서 입력
  - TCP/IP에 응답 전달, 소켓 종료
- **서블릿을 지원하는 WAS 사용하면 비즈니스 로직만 작성하면 됌**
- 서블릿(Servlet)은 **자바(Java)로 웹 요청과 응답을 처리하는 방법에 대한 기술 '규약(Specification)'이자 '인터페이스(Interface)'**

  - 톰캣처럼 서블릿을 지원하는 WAS를 서블릿 컨테이너라고 함
  - 서블릿 컨테이너는 서블릿 객체를 생성, 초기화, 호출, 종료하는 생명주기 관리
  - 서블릿 객체는 싱글톤으로 관리
    - 고객의 요청이 올 때 마다 계속 객체를 생성하는 것은 비효율
    - 최초 로딩 시점에 서블릿 객체를 미리 만들어두고 재활용
    - 모든 고객 요청은 동일한 서블릿 객체 인스턴스에 접근
    - 공유 변수 사용 주의
  - 서블릿 컨테이너 종료시 함께 종료
  - JSP도 서블릿으로 변환 되어서 사용
  - 동시 요청을 위한 멀티 쓰레드 처리 지원

- HTTP 요청, 응답 흐름
  - WAS는 Request, Response 객체를 새로 만들어서 서블릿 객체 호출
  - 개발자는 Request 객체에서 HTTP 요청 정보를 편리하게 꺼내서 사용
  - 개발자는 Response 객체에 HTTP 응답 정보를 편리하게 입력
  - WAS는 Response 객체에 담겨있는 내용으로 HTTP 응답 정보를 생성

### 7. 동시 요청 - 멀티 쓰레드

- 애플리케이션 코드를 하나하나 순차적으로 실행하는 것은 쓰레드
- 자바 메인 메서드를 처음 실행하면 main이라는 이름의 쓰레드가 실행
- 쓰레드가 없다면 자바 애플리케이션 실행이 불가능
- 쓰레드는 한번에 하나의 코드 라인만 수행
- 동시 처리가 필요하면 쓰레드를 추가로 생성

#### 요청 마다 쓰레드 생성

- 장점
  - 동시 요청을 처리할 수 있다.
  - 리소스(CPU, 메모리)가 허용할 때 까지 처리가능
  - 하나의 쓰레드가 지연 되어도, 나머지 쓰레드는 정상 동작한다.
- 단점
  - 쓰레드는 생성 비용은 매우 비싸다.
  - 고객의 요청이 올 때 마다 쓰레드를 생성하면, 응답 속도가 늦어진다.
  - 쓰레드는 컨텍스트 스위칭 비용이 발생한다.
  - 쓰레드 생성에 제한이 없다.
  - 고객 요청이 너무 많이 오면, CPU, 메모리 임계점을 넘어서 서버가 죽을 수 있다.

#### 쓰레드 풀

- 필요한 쓰레드를 쓰레드 풀에 보관하고 관리한다.
- 쓰레드 풀에 생성 가능한 쓰레드의 최대치를 관리한다. 톰캣은 최대 200개 기본 설정 (변경 가능)
- 쓰레드가 필요하면, 이미 생성되어 있는 쓰레드를 쓰레드 풀에서 꺼내서 사용한다.
- 사용을 종료하면 쓰레드 풀에 해당 쓰레드를 반납한다.
- 최대 쓰레드가 모두 사용중이어서 쓰레드 풀에 쓰레드가 없으면?
- 기다리는 요청은 거절하거나 특정 숫자만큼만 대기하도록 설정할 수 있다.
- 장점
  - 쓰레드가 미리 생성되어 있으므로, 쓰레드를 생성하고 종료하는 비용(CPU)이 절약되고, 응답 시간이 빠르다.
  - 생성 가능한 쓰레드의 최대치가 있으므로 너무 많은 요청이 들어와도 기존 요청은 안전하게 처리할 수 있다.
- WAS의 주요 튜닝 포인트는 최대 쓰레드(max thread)수이다.
- 이 값을 너무 낮게 설정하면?
  - 동시 요청이 많으면, 서버 리소스는 여유롭지만, 클라이언트는 금방 응답 지연
  - **요청이 많은 상황에서 최소한 CPU 50% 이상은 사용하게 설정해야 한다.**
- 이 값을 너무 높게 설정하면?
  - 동시 요청이 많으면, CPU, 메모리 리소스 임계점 초과로 서버 다운
- 장애 발생시?
  - 클라우드면 일단 서버부터 늘리고, 이후에 튜닝
  - 클라우드가 아니면 열심히 튜닝
- 적정 숫자는 어떻게 찾나요?
  - 애플리케이션 로직의 복잡도, CPU, 메모리, IO 리소스 상황에 따라 모두 다름
- 성능 테스트
  - 최대한 실제 서비스와 유사하게 성능 테스트 시도
  - 툴: 아파치 ab, 제이미터, nGrinder

#### WAS의 멀티 쓰레드 지원

- 멀티 쓰레드에 대한 부분은 WAS가 처리
- 개발자가 멀티 쓰레드 관련 코드를 신경쓰지 않아도 됨
- 개발자는 마치 싱글 쓰레드 프로그래밍을 하듯이 편리하게 소스 코드를 개발
- 멀티 쓰레드 환경이므로 싱글톤 객체(서블릿, 스프링 빈)는 주의해서 사용

### 8. HTML, HTTP API, CSR, SSR

#### 고정된 HTML 파일, CSS, JS, 이미지, 영상 등을 제공

- /폴더/web/hello.html
- /폴더/web/hello.css
- /폴더/web/hello.js
- /폴더/image/a.jpg
- 이미 생성된 리소스 파일

#### HTML 페이지

- 동적으로 필요한 HTML 파일을 생성해서 전달(JSP, 타임리프)
- 웹 브라우저: HTML 해석

#### HTTP API

- HTML이 아니라 데이터를 전달
- 주로 JSON 형식 사용
- 다양한 시스템에서 호출
- 서버 to 서버
- 앱 클라이언트(아이폰, 안드로이드, PC 앱 등등)
- 다양한 시스템에서 호출
- 데이터만 주고 받음, UI 화면이 필요하면, 클라이언트가 별도 처리

#### 서버사이드 렌더링, 클라이언트 사이드 렌더링

- SSR - 서버 사이드 렌더링

  - HTML 최종 결과를 서버에서 만들어서 웹 브라우저에 전달
  - 주로 정적인 화면에 사용
  - 관련기술: JSP, 타임리프 -> 백엔드 개발자

- CSR - 클라이언트 사이드 렌더링

  - HTML 결과를 자바스크립트를 사용해 웹 브라우저에서 동적으로 생성해서 적용
  - 주로 동적인 화면에 사용, 웹 환경을 마치 앱 처럼 필요한 부분부분 변경할 수 있음
  - 예) 구글 지도, Gmail, 구글 캘린더
  - 관련기술: React, Vue.js -> 웹 프론트엔드 개발자

- 참고
  - React, Vue.js를 CSR + SSR 동시에 지원하는 웹 프레임워크도 있음
  - SSR을 사용하더라도, 자바스크립트를 사용해서 화면 일부를 동적으로 변경 가능

#### 백엔드 개발자 입장에서 UI 기술 어디까지 알아야 하나요?

- 백엔드 - 서버 사이드 렌더링 기술
  - JSP, 타임리프
  - 화면이 정적이고, 복잡하지 않을 때 사용
  - 백엔드 개발자는 서버 사이드 렌더링 기술 학습 필수
- 웹 프론트엔드 - 클라이언트 사이드 렌더링 기술
  - React, Vue.js
  - 복잡하고 동적인 UI 사용
  - 웹 프론트엔드 개발자의 전문 분야
- 선택과 집중
- 백엔드 개발자의 웹 프론트엔드 기술 학습은 옵션
- 백엔드 개발자는 서버, DB, 인프라 등등 수 많은 백엔드 기술을 공부해야 한다.
- 웹 프론트엔드도 깊이있게 잘 하려면 숙련에 오랜 시간이 필요하다.

### 9. 자바 백엔드 웹 기술 역사

#### 과거 기술

- 서블릿 - 1997 - HTML 생성이 어려움
- JSP - 1999 - HTML 생성은 편리하지만, 비즈니스 로직까지 너무 많은 역할 담당
- 서블릿, JSP 조합 MVC 패턴 사용
  - 모델, 뷰 컨트롤러로 역할을 나누어 개발
  - MVC 프레임워크 춘추 전국 시대 - 2000년 초 ~ 2010년 초
  - MVC 패턴 자동화, 복잡한 웹 기술을 편리하게 사용할 수 있는 다양한 기능 지원
  - 스트럿츠, 웹워크, 스프링 MVC(과거 버전)

#### 현재 사용 기술

- 애노테이션 기반의 스프링 MVC 등장
- @Controller
- MVC 프레임워크의 춘추 전국 시대 마무리
- 스프링 부트의 등장
- 스프링 부트는 서버를 내장
- 과거에는 서버에 WAS를 직접 설치하고, 소스는 War 파일을 만들어서 설치한 WAS에 배포
- 스프링 부트는 빌드 결과(Jar)에 WAS 서버 포함 -> 빌드 배포 단순화

- Web Servlet - Spring MVC
- Web Reactive - Spring WebFlux

#### 최신 기술 - 스프링 웹 플럭스(WebFlux)

- 비동기 넌 블러킹 처리
- 최소 쓰레드로 최대 성능 - 쓰레드 컨텍스트 스위칭 비용 효율화
- 함수형 스타일로 개발 - 동시처리 코드 효율화
- 서블릿 기술 사용X
- 그런데 웹 플럭스는 기술적 난이도 매우 높음
- 아직은 RDB 지원 부족
- 일반 MVC의 쓰레드 모델도 충분히 빠르다.
- **실무에서 아직 많이 사용하지는 않음 (전체 1% 이하)**

#### 자바 뷰 템플릿 역사

- HTML을 편리하게 생성하는 뷰 기능
- JSP - 속도 느림, 기능 부족
- 프리마커(Freemarker), Velocity(벨로시티) - 속도 문제 해결, 다양한 기능
- **타임리프(Thymeleaf)**
  - 내추럴 템플릿: HTML의 모양을 유지하면서 뷰 템플릿 적용 가능
  - 스프링 MVC와 강력한 기능 통합
  - **최선의 선택**, 단 성능은 프리마커, 벨로시티가 더 빠름

## 3. 서블릿

### 10. 프로젝트 생성

### 11. Hello 서블릿

- 서블릿은 톰캣 같은 웹 애플리케이션 서버를 직접 설치하고,그 위에 서블릿 코드를 클래스 파일로 빌드해서 올린다음, 톰캣 서버를 실행하면 된다. 하지만 이 과정은 매우 번거롭다.
- **스프링 부트는 톰캣 서버를 내장하고 있으므로, 톰캣 서버 설치 없이 편리하게 서블릿 코드를 실행할 수 있다.**
- 스프링 부트 서블릿 환경 구성
  - `@ServletComponentScan`: 스프링 부트는 서블릿을 직접 등록해서 사용할 수 있도록 지원한다.

### 12. HttpServletRequest - 개요

- 서블릿은 HTTP 요청 메시지를 파싱하여 그 결과를 `HttpServletRequest`객체에 담아서 제공한다.
- START LINE
  - HTTP 메소드
  - URL
  - 쿼리 스트링
  - 스키마, 프로토콜
  - 헤더
  - 헤더 조회
- 바디

  - form 파라미터 형식 조회
  - message body 데이터 직접 조회

- HttpServletRequest 객체는 추가로 여러가지 부가기능도 함께 제공한다.

  - 임시 저장소 기능
    - 해당 HTTP 요청이 시작부터 끝날 때 까지 유지되는 임시 저장소 기능
    - 저장: request.setAttribute(name, value)
    - 조회: request.getAttribute(name)
  - 세션 관리 기능
    - request.getSession(create: true)

### 13. HttpServletRequest - 기본 사용법

- [RequestHeaderServlet.java](./src/main/java/com/mvc/hello/basic/request/RequestHeaderServlet.java)

### 14. HTTP 요청 데이터 - 개요

- HTTP 요청 메시지를 통해 클라이언트에서 서버로 데이터를 전달하는 방법을 알아보자.
- 주로 다음 3가지 방법을 사용한다.
- **GET - 쿼리 파라미터**
  - /url**?username=hello&age=20**
  - 메시지 바디 없이, URL의 쿼리 파라미터에 데이터를 포함해서 전달
  - 예) 검색, 필터, 페이징등에서 많이 사용하는 방식
- **POST - HTML Form**
  - content-type: application/x-www-form-urlencoded
  - 메시지 바디에 쿼리 파리미터 형식으로 전달 username=hello&age=20
  - 예) 회원 가입, 상품 주문, HTML Form 사용
- **HTTP message body에 데이터를 직접 담아서 요청**
  - HTTP API에서 주로 사용, JSON, XML, TEXT
  - 데이터 형식은 주로 JSON 사용
  - POST, PUT, PATCH

### 15. HTTP 요청 데이터 - GET 쿼리 파라미터

- [RequestParamServlet.java](./src/main/java/com/mvc/hello/basic/request/RequestParamServlet.java)

### 16. HTTP 요청 데이터 - POST HTML Form

- content-type: application/x-www-form-urlencoded
- 메시지 바디에 쿼리 파리미터 형식으로 데이터를 전달한다. username=hello&age=20

  - 요청 URL: http://localhost:8080/request-param
  - content-type: application/x-www-form-urlencoded
  - message body: username=hello&age=20

- application/x-www-form-urlencoded 형식은 앞서 GET에서 살펴본 쿼리 파라미터 형식과 같다.
- 따라서 쿼리 파라미터 조회 메서드를 그대로 사용하면 된다.
- 클라이언트(웹 브라우저) 입장에서는 두 방식에 차이가 있지만, 서버 입장에서는 둘의 형식이 동일하므로, request.getParameter() 로 편리하게 구분없이 조회할 수 있다.
- http 메서드가 달라 분리해서 처리해야 할 것 같은데 이부분 신기하다.
  - `@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})` - 요즘 방식도 가능
- 정리하면 request.getParameter() 는 GET URL 쿼리 파라미터 형식도 지원하고, POST HTML Form 형식도 둘 다 지원한다.

- content-type은 HTTP 메시지 바디의 데이터 형식을 지정한다.
  - GET URL 쿼리 파라미터 형식으로 클라이언트에서 서버로 데이터를 전달할 때는 HTTP 메시지 바디를 사용하지 않기 때문에 content-type이 없다.
  - **POST HTML Form 형식으로 데이터를 전달하면 HTTP 메시지 바디에 해당 데이터를 포함해서 보내기 때문에 바디에 포함된 데이터가 어떤 형식인지 content-type을 꼭 지정해야 한다.**
  - 이렇게 폼으로 데이터를 전송하는 형식을 application/x-www-form-urlencoded 라 한다.

### 17. HTTP 요청 데이터 - API 메시지 바디 - 단순 텍스트

- HTTP message body에 데이터를 직접 담아서 요청

  - API에서 주로 사용, JSON, XML, TEXT
  - 데이터 형식은 주로 JSON 사용
  - POST, PUT, PATCH

- inputStream은 byte 코드를 반환한다. byte 코드를 우리가 읽을 수 있는 문자(String)로 보려면 문자표(Charset)를 지정해주어야 한다.
- [RequestBodyStringServlet.java](./src/main/java/com/mvc/hello/basic/request/RequestBodyStringServlet.java)

### 18. HTTP 요청 데이터 - API 메시지 바디 - JSON

- JSON 결과를 파싱해서 사용할 수 있는 자바 객체로 변환하려면 Jackson, Gson 같은 JSON 변환 라이브러리를 추가해서 사용해야 한다. **스프링 부트로 Spring MVC를 선택하면 기본으로 Jackson 라이브러리(ObjectMapper)를 함께 제공**한다.
- HTML form 데이터도 메시지 바디를 통해 전송되므로 직접 읽을 수 있다. 하지만 편리한 파리미터 조회 기능(`request.getParameter(...)`)을 이미 제공하기 때문에 파라미터 조회 기능을 사용하면 된다.
- [RequestBodyJsonServlet.java](./src/main/java/com/mvc/hello/basic/request/RequestBodyJsonServlet.java)

### 19. HttpServletResponse - 기본 사용법

- [ResponseHeaderServlet.java](./src/main/java/com/mvc/hello/basic/response/ResponseHeaderServlet.java)

### 20. HTTP 응답 데이터 - 단순 텍스트, HTML

- [ResponseHtmlServlet.java](./src/main/java/com/mvc/hello/basic/response/ResponseHtmlServlet.java)
- **HTTP 응답으로 HTML을 반환할 때는 content-type을 `text/html`로 지정해야 한다.**

### 21. HTTP 응답 데이터 - API JSON

- [ResponseJsonServlet.java](./src/main/java/com/mvc/hello/basic/response/ResponseJsonServlet.java)
- **HTTP 응답으로 JSON을 반환할 때는 content-type을 `application/json`로 지정해야 한다.**
- Jackson 라이브러리가 제공하는 objectMapper.writeValueAsString() 를 사용하면 객체를 JSON 문자로 변경할 수 있다.
- **`application/json`은 스펙상 utf-8 형식을 사용하도록 정의되어 있다.** 그래서 스펙에서 `charset=utf-8`과 같은 추가 파라미터를 지원하지 않는다.

### 22. 정리

## 4. 서블릿, JSP, MVC 패턴

### 23. 회원 관리 웹 애플리케이션 요구사항

### 24. 서블릿으로 회원 관리 웹 애플리케이션 만들기

### 25. JSP로 회원 관리 웹 애플리케이션 만들기

### 26. MVC 패턴 - 개요

### 27. MVC 패턴 - 적용

### 28. MVC 패턴 - 한계

### 29. 정리

## 5. MVC 프레임워크 만들기

### 30. 프론트 컨트롤러 패턴 소개

### 31. 프론트 컨트롤러 도입 - v1

### 32. View 분리 - v2

### 33. Model 추가 - v3

### 34. 단순하고 실용적인 컨트롤러 - v4

### 35. 유연한 컨트롤러1 - v5

### 36. 유연한 컨트롤러2 - v5

### 37. 정리

## 6. 스프링 MVC - 구조 이해

### 38. 스프링 MVC 전체 구조

### 39. 핸들러 매핑과 핸들러 어댑터

### 40. 뷰 리졸버

### 41. 스프링 MVC - 시작하기

### 42. 스프링 MVC - 컨트롤러 통합

### 43. 스프링 MVC - 실용적인 방식

### 44. 정리

## 7. 스프링 MVC - 기본 기능

### 45. 프로젝트 생성

### 46. 로깅 간단히 알아보기

### 47. 요청 매핑

### 48. 요청 매핑 - API 예시

### 49. HTTP 요청 - 기본, 헤더 조회

### 50. HTTP 요청 파라미터 - 쿼리 파라미터, HTML Form

### 51. HTTP 요청 파라미터 - @RequestParam

### 52. HTTP 요청 파라미터 - @ModelAttribute

### 53. HTTP 요청 메시지 - 단순 텍스트

### 54. HTTP 요청 메시지 - JSON

### 55. 응답 - 정적 리소스, 뷰 템플릿

### 56. HTTP 응답 - HTTP API, 메시지 바디에 직접 입력

### 57. HTTP 메시지 컨버터

### 58. 요청 매핑 헨들러 어뎁터 구조

### 59. 정리

## 8. 스프링 MVC - 웹 페이지 만들기

### 60. 프로젝트 생성

### 61. 요구사항 분석

### 62. 상품 도메인 개발

### 63. 상품 서비스 HTML

### 64. 상품 목록 - 타임리프

### 65. 상품 상세

### 66. 상품 등록 폼

### 67. 상품 등록 처리 - @ModelAttribute

### 68. 상품 수정

### 69. PRG Post/Redirect/Get

### 70. RedirectAttributes

### 71. 정리

## 9. 다음으로
