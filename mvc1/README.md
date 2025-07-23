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

- 회원 저장소는 싱글톤 패턴을 적용했다. 스프링을 사용하면 스프링 빈으로 등록하면 되지만, 지금은 최대한 스프링 없이 순수 서블릿 만으로 구현하는 것이 목적이다.
- 싱글톤 패턴은 객체를 단 하나만 생생해서 공유해야 하므로 생성자를 private 접근자로 막아둔다.

### 24. 서블릿으로 회원 관리 웹 애플리케이션 만들기

- MemberFormServlet 은 단순하게 회원 정보를 입력할 수 있는 HTML Form을 만들어서 응답한다.
- 자바 코드로 HTML을 제공해야 하므로 쉽지 않은 작업이다.

- 지금까지 서블릿과 자바 코드만으로 HTML을 만들어보았다.
- 서블릿 덕분에 동적으로 원하는 HTML을 마음껏 만들 수 있다.
- 정적인 HTML 문서라면 화면이 계속 달라지는 회원의 저장 결과라던가, 회원 목록 같은 동적인 HTML을 만드는 일은 불가능 할 것이다.
- 그런데, 코드에서 보듯이 이것은 매우 복잡하고 비효율 적이다. 자바 코드로 HTML을 만들어 내는 것 보다 차라리 HTML 문서에 동적으로 변경해야 하는 부분만 자바 코드를 넣을 수 있다면 더 편리할 것이다.
- 이것이 바로 템플릿 엔진이 나온 이유이다. 템플릿 엔진을 사용하면 HTML 문서에서 필요한 곳만 코드를 적용해서 동적으로 변경할 수 있다.

### 25. JSP로 회원 관리 웹 애플리케이션 만들기

- `<%@ page contentType="text/html;charset=UTF-8" language="java" %>` - JSP문서라는 뜻이다. JSP 문서는 이렇게 시작해야 한다.
- 서블릿으로 개발할 때는 뷰(View)화면을 위한 HTML을 만드는 작업이 자바 코드에 섞여서 지저분하고 복잡했다.
- JSP를 사용한 덕분에 뷰를 생성하는 HTML 작업을 깔끔하게 가져가고, 중간중간 동적으로 변경이 필요한 부분에만 자바 코드를 적용했다. 그런데 이렇게 해도 해결되지 않는 몇가지 고민이 남는다.

- 회원 저장 JSP를 보자. 코드의 상위 절반은 회원을 저장하기 위한 비즈니스 로직이고, 나머지 하위 절반만 결과를 HTML로 보여주기 위한 뷰 영역이다. 회원 목록의 경우에도 마찬가지다.
- 코드를 잘 보면, JAVA 코드, 데이터를 조회하는 리포지토리 등등 다양한 코드가 모두 JSP에 노출되어 있다. **JSP가 너무 많은 역할을 한다.**
- **비즈니스 로직은 서블릿 처럼 다른곳에서 처리하고, JSP는 목적에 맞게 HTML로 화면(View)을 그리는 일에 집중하도록 하자.** 과거 개발자들도 모두 비슷한 고민이 있었고, **그래서 MVC 패턴이 등장했다.**

### 26. MVC 패턴 - 개요

- 너무 많은 역할
  - 하나의 서블릿이나 JSP만으로 비즈니스 로직과 뷰 렌더링까지 모두 처리하게 되면, 너무 많은 역할을 하게되고, 결과적으로 유지보수가 어려워진다.
  - 비즈니스 로직을 호출하는 부분에 변경이 발생해도 해당 코드를 손대야 하고, UI를 변경 할 일이 있어도 비즈니스 로직이 함께 있는 해당 파일을 수정해야 한다.
- 변경의 라이프 사이클
  - 사실 이게 정말 중요한데, 진짜 문제는 둘 사이에 변경의 라이프 사이클이 다르다는 점이다.
  - 예를 들어서 UI를 일부 수정하는 일과 비즈니스 로직을 수정하는 일은 각각 다르게 발생할 가능성이 매우 높고 대부분 서로에게 영향을 주지 않는다.
- 기능 특화

  - 특히 JSP 같은 뷰 템플릿은 화면을 렌더링 하는데 최적화 되어 있기 때문에 이 부분의 업무만 담당하는 것이 가장 효과적이다.

- Model View Controller
  - MVC 패턴은 지금까지 학습한 것 처럼 하나의 서블릿이나, JSP로 처리하던 것을 컨트롤러(Controller)와 뷰(View)라는 영역으로 서로 역할을 나눈 것을 말한다.
  - 웹 애플리케이션은 보통 이 MVC 패턴을 사용한다.
  - 컨트롤러: HTTP 요청을 받아서 파라미터를 검증하고, 비즈니스 로직을 실행한다. 그리고 뷰에 전달할 결과 데이터를 조회해서 모델에 담는다.
  - 모델: 뷰에 출력할 데이터를 담아둔다. 뷰가 필요한 데이터를 모두 모델에 담아서 전달해주는 덕분에 뷰는 비즈니스 로직이나 데이터 접근을 몰라도 되고, 화면을 렌더링 하는 일에 집중할 수 있다.
  - 뷰: 모델에 담겨있는 데이터를 사용해서 화면을 그리는 일에 집중한다. 여기서는 HTML을 생성하는 부분을 말한다.

#### 여태 모델을 어플리케이션의 데이터 같은 개념으로 생각했는데...

MVC 패턴의 **개념적인 모델(M)**과, **프레임워크에서 실제로 사용하는 모델 객체를 구분**해서 생각해야 합니다.

1.  개념적/광의의 모델 (The "M" in MVC): 이것이 MVC 패턴의 본질적인 '모델'입니다. 애플리케이션의 핵심 데이터와 그 데이터를 처리하는 모든 비즈니스 로직을 의미합니다.

- 데이터베이스에 접근하여 데이터를 조회, 저장, 수정, 삭제합니다.
- 비즈니스 규칙에 따라 데이터를 가공하고 처리합니다.
- 포함하는 영역:
  - 서비스(Service) 계층: 비즈니스 로직 처리 (UserService)
  - 리포지토리(Repository/DAO) 계층: 데이터베이스 접근 (UserRepository)
  - 도메인 객체(Entity/DTO) 계층: 데이터 그 자체 (User 객체)
- 따라서 **"모델이 DB에 접근하는 영역"**이라는 말은 이 넓은 의미의 모델을 가리키는 것입니다.

2. 프레임워크에서의 모델 (데이터 전달 객체):스프링(Spring) MVC와 같은 프레임워크에서 컨트롤러 메서드의 파라미터로 받는 Model 객체는 사용자께서 말씀하신 **"컨트롤러에서 뷰로 넘겨주는 데이터의 집합"**이 맞습니다.

- 컨트롤러가 뷰(View, 예: HTML 템플릿)에 데이터를 전달하기 위해 사용하는 데이터 운반용 상자 또는 **단순한 맵(Map)**입니다.

### 27. MVC 패턴 - 적용

- 서블릿을 컨트롤러로 사용하고, JSP를 뷰로 사용해서 MVC 패턴을 적용해보자.
- Model은 `HttpServletRequest`객체를 사용한다. request는 내부에 데이터 저장소를 가지고 있는데, request.setAttribute() , request.getAttribute() 를 사용하면 데이터를 보관하고, 조회할 수 있다.

- `dispatcher.forward()` : 다른 서블릿이나 JSP로 이동할 수 있는 기능이다. 서버 내부에서 다시 호출이 발생한다.
- `/WEB-INF`: 이 경로안에 JSP가 있으면 외부에서 직접 JSP를 호출할 수 없다. 우리가 기대하는 것은 항상 컨트롤러를 통해서 JSP를 호출하는 것이다.
- redirect vs forward

  - 리다이렉트는 실제 클라이언트(웹 브라우저)에 응답이 나갔다가, 클라이언트가 redirect 경로로 다시 요청한다. 따라서 클라이언트가 인지할 수 있고, URL 경로도 실제로 변경된다.
  - 반면에 포워드는 서버 내부에서 일어나는 호출이기 때문에 클라이언트가 전혀 인지하지 못한다.

- `<%= request.getAttribute("member")%>`로 모델에 저장한 member 객체를 꺼낼 수 있지만, 너무 복잡해진다.

  - JSP는 `${}` 문법을 제공하는데, 이 문법을 사용하면 request의 attribute에 담긴 데이터를 편리하게 조회할 수 있다.

- MVC 덕분에 컨트롤러 로직과 뷰 로직을 확실하게 분리한 것을 확인할 수 있다. 향후 화면에 수정이 발생하면 뷰 로직만 변경하면 된다.

### 28. MVC 패턴 - 한계

- MVC 패턴을 적용한 덕분에 컨트롤러의 역할과 뷰를 렌더링 하는 역할을 명확하게 구분할 수 있다.
- 특히 뷰는 화면을 그리는 역할에 충실한 덕분에, 코드가 깔끔하고 직관적이다. 단순하게 모델에서 필요한 데이터를 꺼내고, 화면을 만들면 된다.

#### MVC 컨트롤러의 단점

- 포워드 중복 - View로 이동하는 코드가 항상 중복 호출되어야 한다. 물론 이 부분을 메서드로 공통화해도 되지만, 해당 메서드도 항상 직접 호출해야 한다.
- ViewPath 중복
  - prefix: /WEB-INF/views/
  - suffix: .jsp
  - jsp가 아닌 thymeleaf 같은 다른 뷰로 변경한다면 전체 코드를 다 변경해야 한다.

````java
RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
dispatcher.forward(request, response);
String viewPath = "/WEB-INF/views/new-form.jsp";

  - 사용하지 않는 코드
    - 다음 코드를 사용할 때도 있고, 사용하지 않을 때도 있다. 특히 response는 현재 코드에서 사용되지 않는다.
    - HttpServletRequest , HttpServletResponse 를 사용하는 코드는 테스트 케이스를 작성하기도 어렵다.

```java
HttpServletRequest request, HttpServletResponse response
````

- 공통 처리가 어렵다.

  - 이 문제를 해결하려면 컨트롤러 호출 전에 먼저 공통 기능을 처리해야 한다.
  - 소위 수문장 역할을 하는 기능이 필요하다.
  - 프론트 컨트롤러(Front Controller) 패턴을 도입하면 이런 문제를 깔끔하게 해결할 수 있다.(입구를 하나로!)

- 스프링 MVC의 핵심도 바로 이 프론트 컨트롤러에 있다.

### 29. 정리

## 5. MVC 프레임워크 만들기

### 30. 프론트 컨트롤러 패턴 소개

- 프론트 컨트롤러 서블릿 하나로 클라이언트의 요청을 받음
- 프론트 컨트롤러가 요청에 맞는 컨트롤러를 찾아서 호출
- **입구를 하나로 해서 공통 처리 가능**
- 프론트 컨트롤러를 제외한 나머지 컨트롤러는 서블릿을 사용하지 않아도 됨

- **스프링 웹 MVC의 DispatcherServlet이 FrontController 패턴으로 구현되어 있음**
- 스프링도 용빼는 재주가 있는게 아니라 서블릿이 앞단에 있는 것

### 31. 프론트 컨트롤러 도입 - v1

- 서블릿과 비슷한 모양의 컨트롤러 인터페이스를 도입한다. 각 컨트롤러들은 이 인터페이스를 구현하면 된다.
- 프론트 컨트롤러는 이 인터페이스를 호출해서 구현과 관계없이 로직의 일관성을 가져갈 수 있다.
- 하위 모든 요청을 프론트 컨트롤러 서블릿에서 받아, url에 맞는 controller를 가져와 메서드 실행

### 32. View 분리 - v2

- 모든 컨트롤러에서 뷰로 이동하는 부분에 중복이 있는데, 이 부분을 깔끔하게 분리하기 위해 별도로 뷰를 처리하는 객체를 만들자.
  - ControllerV2의 반환 타입이 MyView 이므로 프론트 컨트롤러는 컨트롤러의 호출 결과로 MyView 를 반환 받는다.
  - 그리고 view.render() 를 호출하면 forward 로직을 수행해서 JSP가 실행된다.
- 프론트 컨트롤러의 도입으로 MyView 객체의 render() 를 호출하는 부분을 모두 일관되게 처리할 수 있다. 각각의 컨트롤러는 MyView 객체를 생성만 해서 반환하면 된다.

### 33. Model 추가 - v3

- 서블릿 종속성 제거

  - 컨트롤러 입장에서 HttpServletRequest, HttpServletResponse이 꼭 필요할까?
  - 요청 파라미터 정보는 자바의 Map으로 대신 넘기도록 하면 지금 구조에서는 컨트롤러가 서블릿 기술을 몰라도 동작할 수 있다.
  - 그리고 request 객체를 Model로 사용하는 대신에 별도의 Model 객체를 만들어서 반환하면 된다.
  - 이렇게 하면 구현 코드도 매우 단순해지고, 테스트 코드 작성이 쉽다.

- 뷰 이름 중복 제거
  - 컨트롤러에서 지정하는 뷰 이름에 중복이 있는 것을 확인할 수 있다.
  - 컨트롤러는 뷰의 논리 이름을 반환하고, 실제 물리 위치의 이름은 프론트 컨트롤러에서 처리하도록 단순화 하자.
  - 이렇게 해두면 향후 뷰의 폴더 위치가 함께 이동해도 프론트 컨트롤러만 고치면 된다.

### 34. 단순하고 실용적인 컨트롤러 - v4

- 컨트톨러 인터페이스를 구현하는 개발자 입장에서 보면, 항상 ModelView 객체를 생성하고 반환해야 하는 부분이 조금은 번거롭다.
- 좋은 프레임워크는 아키텍처도 중요하지만, 그와 더불어 실제 개발하는 개발자가 단순하고 편리하게 사용할 수 있어야 한다. 소위 실용성이 있어야 한다.
- 프레임워크나 공통 기능이 수고로워야 사용하는 개발자가 편리해진다.

### 35. 유연한 컨트롤러1 - v5

- 지금까지 우리가 개발한 프론트 컨트롤러는 한가지 방식의 컨트롤러 인터페이스만 사용할 수 있다.
- `ControllerV3` , `ControllerV4`는 완전히 다른 인터페이스이다.
  - 따라서 호환이 불가능하다. 마치 v3는 110v이고, v4는 220v 전기 콘센트 같은 것이다. 이럴 때 사용하는 것이 바로 어댑터이다.
- 어댑터 패턴을 사용해서 프론트 컨트롤러가 다양한 방식의 컨트롤러를 처리할 수 있도록 변경해보자.

- 핸들러 어댑터: 중간에 어댑터 역할을 하는 어댑터가 추가되었는데 이름이 핸들러 어댑터이다. 여기서 어댑터 역할을 해주는 덕분에 다양한 종류의 컨트롤러를 호출할 수 있다.
- 핸들러: 컨트롤러의 이름을 더 넓은 범위인 핸들러로 변경했다. 그 이유는 이제 어댑터가 있기 때문에 꼭 컨트롤러의 개념 뿐만 아니라 어떠한 것이든 해당하는 종류의 어댑터만 있으면 다 처리할 수 있기 때문이다.

- 이전에는 컨트롤러를 직접 매핑해서 사용했다. 그런데 이제는 어댑터를 사용하기 때문에, 컨트롤러 뿐만 아니라 어댑터가 지원하기만 하면, 어떤 것이라도 URL에 매핑해서 사용할 수 있다. 그래서 이름을 컨트롤러에서 더 넒은 범위의 핸들러로 변경했다.

### 36. 유연한 컨트롤러2 - v5

- v4 핸들러를 구현해, 어댑터에 연동

### 37. 정리

- 지금까지 v1 ~ v5로 점진적으로 프레임워크를 발전시켜 왔다.
- v1: 프론트 컨트롤러를 도입
  - 기존 구조를 최대한 유지하면서 프론트 컨트롤러를 도입
- v2: View 분류
  - 단순 반복 되는 뷰 로직 분리
- v3: Model 추가
  - 서블릿 종속성 제거
  - 뷰 이름 중복 제거
- v4: 단순하고 실용적인 컨트롤러
  - v3와 거의 비슷
  - 구현 입장에서 ModelView를 직접 생성해서 반환하지 않도록 편리한 인터페이스 제공
- v5: 유연한 컨트롤러

  - 어댑터 도입
  - 어댑터를 추가해서 프레임워크를 유연하고 확장성 있게 설계

- 만약 애노테이션을 사용해서 컨트롤러를 편리하게 사용할 수 있게 하려면 어떻게 해야할까?
- 바로 애노테이션을 지원하는 어댑터를 추가하면 된다!
- **다형성과 어댑터 덕분에 기존 구조를 유지하면서, 프레임워크의 기능을 확장할 수 있다.**
- 스프링 MVC는 지금까지 우리가 학습한 내용과 거의 같은 구조를 가지고 있다.

1. `@RequestMapping`으로 주소와 메서드를 매핑 🗺️
   1. 애플리케이션이 시작될 때, 스프링은 **RequestMappingHandlerMapping**이라는 부품을 사용해서 모든 `@Controller`를 스캔합니다.
   2. 클래스 내부에서 `@RequestMapping`이 붙은 메서드를 모두 찾습니다.
   3. `@RequestMapping("/hello")` 어노테이션을 보고 "아, /hello 라는 URL 요청이 오면 이 메서드를 실행해야 하는구나!" 라는 정보를 미리 지도(Map)처럼 저장해 둡니다.
2. 리플렉션으로 실제 메서드 호출
   1. 실제로 사용자가 /hello로 요청을 보내면, 이제 DispatcherServlet이 이전 단계에서 만들어진 지도를 보고 실행할 메서드를 찾아냅니다.
   2. 그 후 **RequestMappingHandlerAdapter**가 그 메서드를 실행할 차례입니다.
   3. `HandlerAdapter`는 "아, HTTP 요청 파라미터에서 name 값을 꺼내서 이 자리에 넣어줘야겠구나!" 라고 판단합니다.
   4. 필요한 모든 파라미터를 준비한 뒤, 이전 답변에서 본 것처럼 Java 리플렉션을 사용해 최종적으로 메서드를 호출합니다.
   5. `method.invoke(controllerInstance, "kim")`;

## 6. 스프링 MVC - 구조 이해

### 38. 스프링 MVC 전체 구조

- 직접 만든 프레임워크 스프링 MVC 비교

  - `FrontController` - `DispatcherServlet`
  - `handlerMappingMap` - `HandlerMapping`
  - `MyHandlerAdapter` - `HandlerAdapter`
  - `ModelView` - `ModelAndView`
  - `viewResolver` - `ViewResolver`
  - `MyView` - `View`

- 스프링 MVC도 프론트 컨트롤러 패턴으로 구현되어 있다.

  - 스프링 MVC의 프론트 컨트롤러가 바로 디스패처 서블릿(DispatcherServlet)이다.
  - `DispatcherServlet` 도 부모 클래스에서 `HttpServlet`을 상속 받아서 사용하고, 서블릿으로 동작한다.
  - 스프링 부트는 DispatcherServlet 을 서블릿으로 자동으로 등록하면서 모든 경로( urlPatterns="/" )에 대해서 매핑한다.
  - 더 자세한 경로가 우선순위가 높아 기존에 등록한 서블릿도 함께 동작한다.

- 요청 흐름

  - 서블릿이 호출되면 HttpServlet 이 제공하는 service() 가 호출된다.
  - 스프링 MVC는 DispatcherServlet 의 부모인 FrameworkServlet 에서 service() 를 오버라이드 해두었다.
  - FrameworkServlet.service() 를 시작으로 여러 메서드가 호출되면서 DispatcherServlet.doDispatch() 가 호출된다.

- `DispatcherServlet`의 핵심인 `doDispatch()` 코드를 간단히 분석해보자.

  1. 핸들러 조회: 핸들러 매핑을 통해 요청 URL에 매핑된 핸들러(컨트롤러)를 조회한다.
  2. 핸들러 어댑터 조회: 핸들러를 실행할 수 있는 핸들러 어댑터를 조회한다.
  3. 핸들러 어댑터 실행: 핸들러 어댑터를 실행한다.
  4. 핸들러 실행: 핸들러 어댑터가 실제 핸들러를 실행한다.
  5. ModelAndView 반환: 핸들러 어댑터는 핸들러가 반환하는 정보를 ModelAndView로 변환해서 반환한다.
  6. viewResolver 호출: 뷰 리졸버를 찾고 실행한다.
     JSP의 경우: InternalResourceViewResolver 가 자동 등록되고, 사용된다.
  7. View 반환: 뷰 리졸버는 뷰의 논리 이름을 물리 이름으로 바꾸고, 렌더링 역할을 담당하는 뷰 객체를 반환한다.
     JSP의 경우 InternalResourceView(JstlView) 를 반환하는데, 내부에 forward() 로직이 있다.
  8. 뷰 렌더링: 뷰를 통해서 뷰를 렌더링 한다.

- 스프링 MVC는 코드 분량도 매우 많고, 복잡해서 내부 구조를 다 파악하는 것은 쉽지 않다.
- 그래도 핵심 동작방식을 알아두어야 향후 문제가 발생했을 때 어떤 부분에서 문제가 발생했는지 쉽게 파악하고, 문제를 해결할 수 있다.
- 그리고 확장 포인트가 필요할 때, 어떤 부분을 확장해야 할지 감을 잡을 수 있다.
- 지금은 전체적인 구조가 이렇게 되어 있구나 하고 이해하면 된다.

### 39. 핸들러 매핑과 핸들러 어댑터

- Controller 인터페이스로 컨트롤러 구현

  - 스프링도 처음에는 이런 딱딱한 형식의 컨트롤러를 제공했다.
  - @Component : 이 컨트롤러는 /springmvc/old-controller 라는 이름의 스프링 빈으로 등록되었다.
  - 빈의 이름으로 URL을 매핑
  -

- HandlerMapping(핸들러 매핑)

  - 핸들러 매핑에서 이 컨트롤러를 찾을 수 있어야 한다.
  - 예) 스프링 빈의 이름으로 핸들러를 찾을 수 있는 핸들러 매핑이 필요하다.
  - 0 = RequestMappingHandlerMapping : 애노테이션 기반의 컨트롤러인 @RequestMapping에서 사용
  - 1 = BeanNameUrlHandlerMapping : 스프링 빈의 이름으로 핸들러를 찾는다.

- HandlerAdapter(핸들러 어댑터)

  - 핸들러 매핑을 통해서 찾은 핸들러를 실행할 수 있는 핸들러 어댑터가 필요하다.
  - 예) Controller 인터페이스를 실행할 수 있는 핸들러 어댑터를 찾고 실행해야 한다.
  - 0 = RequestMappingHandlerAdapter : 애노테이션 기반의 컨트롤러인 @RequestMapping에서 사용
  - 1 = HttpRequestHandlerAdapter : HttpRequestHandler 처리
  - 2 = SimpleControllerHandlerAdapter : Controller 인터페이스(애노테이션X, 과거에 사용) 처리

- 요청 처리를 위해 일반적으로 Handler를 찾고 실행한 뒤 View를 찾는 과정에서 핵심적인 순서
- **DispatcherServlet → HandlerMapping → HandlerAdapter → ViewResolver**

1. 핸들러 매핑으로 핸들러 조회
   1. HandlerMapping 을 순서대로 실행해서, 핸들러를 찾는다.
   2. 이 경우 빈 이름으로 핸들러를 찾아야 하기 때문에 이름 그대로 빈 이름으로 핸들러를 찾아주는 BeanNameUrlHandlerMapping 가 실행에 성공하고 핸들러인 OldController 를 반환한다.
2. 핸들러 어댑터 조회
   1. HandlerAdapter 의 supports() 를 순서대로 호출한다.
   2. SimpleControllerHandlerAdapter 가 Controller 인터페이스를 지원하므로 대상이 된다.
3. 핸들러 어댑터 실행
   1. 디스패처 서블릿이 조회한 SimpleControllerHandlerAdapter 를 실행하면서 핸들러 정보도 함께 넘겨준다.
   2. SimpleControllerHandlerAdapter 는 핸들러인 OldController 를 내부에서 실행하고, 그 결과를 반환한다.

- OldController 를 실행하면서 사용된 객체는 다음과 같다.

  - HandlerMapping = BeanNameUrlHandlerMapping
  - HandlerAdapter = SimpleControllerHandlerAdapter

- 가장 우선순위가 높은 핸들러 매핑과 핸들러 어댑터는 `RequestMappingHandlerMapping`,`RequestMappingHandlerAdapter`이다.
- @RequestMapping 의 앞글자를 따서 만든 이름인데, 이것이 바로 지금 스프링에서 주로 사용하는 애노테이션 기반의 컨트롤러를 지원하는 매핑과 어댑터이다.
- 실무에서는 99.9% 이 방식의 컨트롤러를 사용한다.

### 40. 뷰 리졸버

- 결국 뷰 이름을 뷰 리졸버가 실제경로로 변환해 찾고, 적절한 뷰로 해당 파일을 처리

1. 핸들러 어댑터 호출
   핸들러 어댑터를 통해 new-form 이라는 논리 뷰 이름을 획득한다.

2. ViewResolver 호출
   new-form 이라는 뷰 이름으로 viewResolver를 순서대로 호출한다.
   BeanNameViewResolver 는 new-form 이라는 이름의 스프링 빈으로 등록된 뷰를 찾아야 하는데 없다.
   InternalResourceViewResolver 가 호출된다.

3. InternalResourceViewResolver
   이 뷰 리졸버는 InternalResourceView 를 반환한다.

4. 뷰 - InternalResourceView
   InternalResourceView 는 JSP처럼 포워드 forward() 를 호출해서 처리할 수 있는 경우에 사용한다.

5. view.render()
   view.render() 가 호출되고 InternalResourceView 는 forward() 를 사용해서 JSP를 실행한다.

### 41. 스프링 MVC - 시작하기

- 스프링이 제공하는 컨트롤러는 애노테이션 기반으로 동작해서, 매우 유연하고 실용적이다.
  - 과거에는 자바 언어에 애노테이션이 없기도 했고, 스프링도 처음부터 이런 유연한 컨트롤러를 제공한 것은 아니다.

#### @RequestMapping

- 스프링은 애노테이션을 활용한 매우 유연하고, 실용적인 컨트롤러를 만들었는데 이것이 바로 `@RequestMapping`애노테이션을 사용하는 컨트롤러이다.
  - 여담이지만 과거에는 스프링 프레임워크가 MVC 부분이 약해서 스프링을 사용하더라도 MVC 웹 기술은 스트럿츠 같은 다른 프레임워크를 사용했지만 @RequestMapping 기반의 애노테이션 컨트롤러가 등장하면서, MVC 부분도 스프링의 완승으로 끝이 났다.
- 가장 우선순위가 높은 핸들러 매핑과 핸들러 어댑터는 `RequestMappingHandlerMapping`, `RequestMappingHandlerAdapter`이다.
- 애노테이션 기반의 컨트롤러를 지원하는 핸들러 매핑과 어댑터이다. 실무에서는 99.9% 이 방식의 컨트롤러를 사용한다.

- @Controller : 스프링이 자동으로 스프링 빈으로 등록한다.
  - 스프링 MVC에서 애노테이션 기반 컨트롤러로 인식한다.
- @RequestMapping : 요청 정보를 매핑한다. 해당 URL이 호출되면 이 메서드가 호출된다.
  - 애노테이션을 기반으로 동작하기 때문에, 메서드의 이름은 임의로 지으면 된다.
- ModelAndView : 모델과 뷰 정보를 담아서 반환하면 된다.

### 42. 스프링 MVC - 컨트롤러 통합

- `@RequestMapping`을 잘 보면 클래스 단위가 아니라 메서드 단위에 적용된 것을 확인할 수 있다.
- 따라서 컨트롤러 클래스를 유연하게 하나로 통합할 수 있다.
- 클래스 레벨에 다음과 같이 `@RequestMapping`을 두면 메서드 레벨과 조합이 된다.

```java
@Controller
@RequestMapping("/springmvc/v2/members")
```

### 43. 스프링 MVC - 실용적인 방식

- `Model` 파라미터
- ViewName 직접 반환
- `@RequestParam` 사용
  - 스프링은 HTTP 요청 파라미터를 @RequestParam 으로 받을 수 있다.
  - @RequestParam("username") 은 request.getParameter("username") 와 거의 같은 코드라 생각하면 된다.
  - GET 쿼리 파라미터, POST Form 방식을 모두 지원한다.
- `@RequestMapping`, `@GetMapping`, `@PostMapping`...
  - `@RequestMapping`은 URL만 매칭하는 것이 아니라, HTTP Method도 함께 구분할 수 있다.
  - 이것을 `@GetMapping` , `@PostMapping` 으로 더 편리하게 사용할 수 있다.

#### @RequestMapping 어노테이션이 어떻게 return ModelAndView or String 처리나 파라미터를 다양하게 받을 수 있게 하는지

- @RequestMapping이 붙은 메서드가 다양한 파라미터를 받고 여러 타입으로 값을 반환할 수 있는 것은 스프링 MVC의 **HandlerMethodArgumentResolver**와 HandlerMethodReturnValueHandler 덕분입니다.

1. 파라미터 처리: HandlerMethodArgumentResolver

   - 역할: 컨트롤러 메서드의 파라미터를 해석하고, HTTP 요청에서 적절한 값을 추출하여 넘겨주는 역할을 합니다.
   - 스프링은 수십 개의 기본 ArgumentResolver 구현체를 가지고 있습니다.
   - DispatcherServlet이 컨트롤러를 호출하기 직전에, 등록된 ArgumentResolver들을 하나씩 확인하며 "이 파라미터를 처리할 수 있니?"라고 물어봅니다.
   - @RequestParam이 붙은 파라미터 → RequestParamMethodArgumentResolver가 처리
   - @PathVariable이 붙은 파라미터 → PathVariableMethodArgumentResolver가 처리
   - Model 타입의 파라미터 → ModelMethodProcessor가 처리
   - HttpServletRequest 타입의 파라미터 → ServletRequestMethodArgumentResolver가 처리
   - 각각의 ArgumentResolver는 자신이 맡은 어노테이션이나 타입을 보고, 요청에서 값을 꺼내거나 필요한 객체를 생성해서 컨트롤러 메서드에 전달해 줍니다.

2. 반환 값 처리: HandlerMethodReturnValueHandler
   - 역할: 컨트롤러 메서드가 반환하는 값을 보고, 이후의 동작(뷰 렌더링, JSON 응답 등)을 결정합니다.
   - 컨트롤러 메서드 실행이 끝나면, 스프링은 반환된 값(객체)을 처리할 수 있는 ReturnValueHandler를 찾습니다.
   - 반환 타입이 String일 경우 → ViewNameMethodReturnValueHandler가 "뷰 이름이구나!"라고 판단하고 ModelAndView 객체에 뷰 이름을 저장합니다.
   - 반환 타입이 ModelAndView일 경우 → ModelAndViewMethodReturnValueHandler가 처리합니다.
   - @ResponseBody가 붙어있거나 반환 타입이 HttpEntity일 경우 → RequestResponseBodyMethodProcessor가 객체를 JSON 등으로 변환하여 HTTP 응답 본문에 직접 써줍니다.

- 이 로직들은 RequestMappingHandlerAdapter 클래스가 총괄하여 관리합니다.

#### 위 과정이 런타임에 동작하는지, 맞다면 리플렉션 API를 사용해서 가능한지

- 해당 기능은 런타임 중에 매 요청마다 동작하며, 이것은 Java의 리플렉션(Reflection) API 때문에 가능합니다.

  - 스프링은 매번 HTTP 요청이 들어올 때마다 다음과 같은 일을 반복합니다.
  - 이 요청을 처리할 컨트롤러 메서드를 찾는다.
  - 스프링은 리플렉션 API를 사용해 컴파일 시점에 저장된 메서드의 설계도(메타데이터)를 읽습니다.
  - 해당 메서드의 파라미터 목록을 확인한다. (@RequestParam, @PathVariable 등이 있는지)
  - 각 파라미터에 맞는 ArgumentResolver를 사용해 요청에서 실제 값을 추출한다.
  - 추출한 값들을 가지고 메서드를 실행한다.
  - 이 과정은 요청에 따라 동적으로 처리되어야 하므로 반드시 런타임에 일어납니다.

- 리플렉션 API의 역할
  - 리플렉션은 프로그램 실행 중에 클래스의 구조(메서드, 필드, 어노테이션 등)를 분석하고 조작할 수 있게 해주는 강력한 Java API입니다.
  - 스프링은 이 리플렉션을 다음과 같이 활용합니다.
  - 메서드 및 파라미터 분석: 런타임에 컨트롤러 메서드(Method 객체)를 가져와 어떤 파라미터들이 있는지, 각 파라미터에 어떤 어노테이션이 붙어있는지 동적으로 확인합니다. (method.getParameters(), parameter.getAnnotations())
  - 어노테이션 확인: parameter.isAnnotationPresent(RequestParam.class)와 같은 코드를 통해 특정 어노테이션의 존재 여부를 확인하고, 그에 맞는 ArgumentResolver를 선택합니다.
  - 메서드 최종 호출: 모든 파라미터 값이 준비되면, method.invoke(controllerInstance, ...)를 통해 최종적으로 컨트롤러 메서드를 실행합니다.
- 결론적으로, 어노테이션 자체는 단순한 '표식'일 뿐이며, 런타임에 스프링이 리플렉션 API를 사용해 이 표식들을 읽고, 그 의미에 따라 동적으로 값을 준비하고 메서드를 호출하는 것입니다.

### 44. 정리
