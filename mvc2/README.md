# 스프링 MVC 1편 - 백엔드 웹 개발 핵심 기술

## 7. 스프링 MVC - 기본 기능

### 45. 프로젝트 생성

- Packaging는 War가 아니라 Jar를 선택해주세요. JSP를 사용하지 않기 때문에 Jar를 사용하는 것이 좋습니다.

  - 앞으로 스프링 부트를 사용하면 이 방식을 주로 사용하게 됩니다.
  - Jar를 사용하면 항상 내장 서버(톰캣등)를 사용하고, webapp 경로도 사용하지 않습니다.
  - 내장 서버 사용에 최적화 되어 있는 기능입니다.
  - War를 사용하면 내장 서버도 사용가능 하지만, 주로 외부 서버에 배포하는 목적으로 사용합니다.

- War는 톰캣을 별도로 설치할 때, JSP를 사용할 때 사용

- 스프링 부트에 Jar를 사용하면 /resources/static/ 위치에 index.html 파일을 두면 Welcome 페이지로 처리해준다.
  - 스프링 부트가 지원하는 정적 컨텐츠 위치에 /index.html 이 있으면 된다.

### 46. 로깅 간단히 알아보기

- 스프링 부트 라이브러리를 사용하면 스프링 부트 로깅 라이브러리(`spring-boot-starter-logging`)가 함께 포함된다.
  - SLF4J - http://www.slf4j.org
  - Logback - http://logback.qos.ch
- 로그 라이브러리는 `Logback`, `Log4J`, `Log4J2` 등등 수 많은 라이브러리가 있는데, 그것을 통합해서 인터페이스로 제공하는 것이 바로 `SLF4J` 라이브러리다.
- **`SLF4J`는 인터페이스이고, 그 구현체로 `Logback` 같은 로그 라이브러리를 선택하면 된다.**
- 실무에서는 스프링 부트가 기본으로 제공하는 Logback을 대부분 사용한다.

- `@RestController`

  - `@Controller` 는 반환 값이 String 이면 뷰 이름으로 인식된다.
  - 그래서 뷰를 찾고 뷰가 랜더링 된다.
  - `@RestController` 는 반환 값으로 뷰를 찾는 것이 아니라, HTTP 메시지 바디에 바로 입력한다.

- 1️⃣ `TRACE`: 가장 상세한 로그. 개발 시 디버깅 용도로 사용.
- 2️⃣ `DEBUG`: 디버그용 로그. 로직 흐름이나 변수 상태 확인용.
- 3️⃣ `INFO`: 정보성 로그. 정상 동작 중의 주요 이벤트 기록.
- 4️⃣ `WARN`: 경고. 문제는 없지만 주의가 필요한 상황.
- 5️⃣ `ERROR`: 에러 발생. 기능은 실패했지만 시스템은 살아있음.

- 올바른 로그 사용법

  - log.debug("data="+data)
    - 로그 출력 레벨을 info로 설정해도 해당 코드에 있는 "data="+data가 실제 실행이 되어 버린다.
    - **결과적으로 문자 더하기 연산이 발생한다.**
  - log.debug("data={}", data)
    - 로그 출력 레벨을 info로 설정하면 아무일도 발생하지 않는다. 따라서 앞과 같은 의미없는 연산이 발생하지 않는다.

- `@Slf4j == private final Logger log = LoggerFactory.getLogger(getClass())`

- 로그 사용시 장점
  - 쓰레드 정보, 클래스 이름 같은 부가 정보를 함께 볼 수 있고, 출력 모양을 조정할 수 있다.
  - 로그 레벨에 따라 개발 서버에서는 모든 로그를 출력하고, 운영서버에서는 출력하지 않는 등 로그를 상황에 맞게 조절할 수 있다.
  - 시스템 아웃 콘솔에만 출력하는 것이 아니라, 파일이나 네트워크 등, 로그를 별도의 위치에 남길 수 있다. 특히 파일로 남길 때는 일별, 특정 용량에 따라 로그를 분할하는 것도 가능하다.
  - 성능도 일반 System.out보다 좋다. (내부 버퍼링, 멀티 쓰레드 등등)
- 그래서 실무에서는 꼭 로그를 사용해야 한다.

### 47. 요청 매핑

- [MappingController](./src/main/java/com/spring/mvc/basic/requestmapping/MappingController.java)
  - 정말 다양한 매핑 방식이 있다.
  - PathVariable 이후로는 처음 봄...

### 48. 요청 매핑 - API 예시

- [MappingClassController](./src/main/java/com/spring/mvc/basic/requestmapping/MappingClassController.java)
- 매핑 연습

### 49. HTTP 요청 - 기본, 헤더 조회

- HttpServletRequest
- HttpServletResponse
- HttpMethod: HTTP 메서드를 조회한다. org.springframework.http.HttpMethod
- Locale : Locale 정보를 조회한다.
- @RequestHeader MultiValueMap<String, String> headerMap
  - 모든 HTTP 헤더를 MultiValueMap 형식으로 조회한다.
- @RequestHeader("host") String host
  - 특정 HTTP 헤더를 조회한다.
  - 필수 값 여부: required
  - 기본 값 속성: defaultValue
- @CookieValue(value = "myCookie", required = false) String cookie
  - 특정 쿠키를 조회한다.
  - 필수 값 여부: required
  - 기본 값: defaultValue
- MultiValueMap

  - MAP과 유사한데, 하나의 키에 여러 값을 받을 수 있다.
  - HTTP header, HTTP 쿼리 파라미터와 같이 하나의 키에 여러 값을 받을 때 사용한다.
  - keyA=value1&keyA=value2

- @Controller 의 사용 가능한 파라미터 목록은 다음 공식 메뉴얼에서 확인할 수 있다.
  - https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-ann-arguments
- @Controller 의 사용 가능한 응답 값 목록은 다음 공식 메뉴얼에서 확인할 수 있다.
  - https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-ann-return-types

### 50. HTTP 요청 파라미터 - 쿼리 파라미터, HTML Form

- **클라이언트에서 서버로 요청 데이터를 전달할 때는 주로 다음 3가지 방법을 사용한다.**
- GET - 쿼리 파라미터
  - /url**?username=hello&age=20**
  - 메시지 바디 없이, URL의 쿼리 파라미터에 데이터를 포함해서 전달
  - 예) 검색, 필터, 페이징등에서 많이 사용하는 방식
- POST - HTML Form
  - content-type: application/x-www-form-urlencoded
  - 메시지 바디에 쿼리 파리미터 형식으로 전달 username=hello&age=20
  - 예) 회원 가입, 상품 주문, HTML Form 사용
- HTTP message body에 데이터를 직접 담아서 요청

  - HTTP API에서 주로 사용, JSON, XML, TEXT
  - 데이터 형식은 주로 JSON 사용

- 요청 파라미터 - 쿼리 파라미터, HTML Form
  - HttpServletRequest 의 request.getParameter() 를 사용하면 다음 두가지 요청 파라미터를 조회할 수 있다.
  - 이것을 간단히 요청 파라미터(request parameter) 조회라 한다.
  - 지금부터 스프링으로 요청 파라미터를 조회하는 방법을 단계적으로 알아보자.
  - [RequestParamController](./src/main/java/com/spring/mvc/basic/request/RequestParamController.java)

### 51. HTTP 요청 파라미터 - @RequestParam

- String , int , Integer 등의 단순 타입이면 `@RequestParam`도 생략 가능

  - 이렇게 애노테이션을 완전히 생략해도 되는데, 너무 없는 것도 약간 과하다는 주관적 생각이 있다.
  - `@RequestParam` 이 있으면 명확하게 요청 파리미터에서 데이터를 읽는 다는 것을 알 수 있다.

- 주의! - 파라미터 이름만 사용

  - /request-param-required?username=
  - 파라미터 이름만 있고 값이 없는 경우 빈문자로 통과

- 주의! - 기본형(primitive)에 `null` 입력

  - /request-param 요청
  - `@RequestParam`(required = false) int age
  - `null` 을 `int` 에 입력하는 것은 불가능(500 예외 발생)
  - 따라서 `null` 을 받을 수 있는 `Integer` 로 변경하거나, 또는 다음에 나오는 `defaultValue`사용

- 파라미터를 Map, MultiValueMap으로 조회할 수 있다.
  - 파라미터의 값이 1개가 확실하다면 Map 을 사용해도 되지만, 그렇지 않다면 MultiValueMap 을 사용하자.

### 52. HTTP 요청 파라미터 - @ModelAttribute

- 실제 개발을 하면 요청 파라미터를 받아서 필요한 객체를 만들고 그 객체에 값을 넣어주어야 한다.
- 스프링은 이 과정을 완전히 자동화해주는 `@ModelAttribute` 기능을 제공한다.

- `@ModelAttribute` 는 생략할 수 있다.
  - `@RequestParam`도 생략할 수 있으니 혼란이 발생할 수 있다.
- 스프링은 해당 생략시 다음과 같은 규칙을 적용한다.
  - String , int , Integer 같은 단순 타입 = `@RequestParam`
  - 나머지 = `@ModelAttribute` (argument resolver 로 지정해둔 타입 외)

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
