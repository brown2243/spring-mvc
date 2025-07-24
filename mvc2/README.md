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

#### 모델 어트리뷰트 방식이 어떻게 가능한지

- 스프링은 자바빈(JavaBeans) 명명 규칙을 사용하여 파라미터 이름으로부터 호출할 세터(setter) 메서드 이름을 추론해냅니다.
- @ModelAttribute가 붙은 객체에 값을 바인딩할 때, 스프링 내부의 **DataBinder**라는 부품이 다음과 같은 순서로 동작합니다.

1. 객체 생성: 먼저, 파라미터로 선언된 객체(예: new UserDto())의 인스턴스를 기본 생성자로 만듭니다.
2. 파라미터 이름 변환 및 세터 추론 (핵심)
   1. HTTP 요청으로 들어온 파라미터 이름(예: user_name)을 가져옵니다.
   2. 이 이름을 자바의 카멜 케이스(camelCase) 필드명(userName)으로 변환합니다.
   3. 자바빈 명명 규칙에 따라, userName 필드에 값을 설정하는 세터 메서드의 이름은 **setUserName**일 것이라고 추론합니다. (첫 글자를 대문자로 바꾸고 앞에 'set'을 붙임)
3. 리플렉션을 이용한 메서드 호출
   1. Java의 리플렉션 API를 사용해 UserDto 클래스에 setUserName(String name)이라는 이름과 시그니처를 가진 메서드가 실제로 존재하는지 찾습니다.
   2. 메서드를 찾으면, 리플렉션을 통해 해당 메서드를 호출하면서 HTTP 요청 파라미터의 값을 전달합니다. (method.invoke(userDto, "kim"))

- 요청 파라미터: item_price=1000
- 스프링의 추론 과정:
  - item_price → itemPrice (카멜 케이스 변환)
  - itemPrice → setItemPrice (세터 메서드 이름 추론)
  - 리플렉션으로 setItemPrice(int price) 메서드를 찾아서 호출

결론적으로, 스프링은 모든 경우의 수를 미리 등록해두는 것이 아니라, '파라미터 이름 → 카멜 케이스 필드명 → set + 필드명' 이라는 정해진 규칙에 따라 동적으로 메서드 이름을 만들어내고, 리플렉션을 통해 해당 메서드를 찾아 실행하는 것입니다. 이 덕분에 다양한 형식의 파라미터 이름을 유연하게 처리할 수 있습니다.

### 53. HTTP 요청 메시지 - 단순 텍스트

- [RequestBodyStringController](./src/main/java/com/spring/mvc/basic/request/RequestBodyStringController.java)

- **요청 파라미터와 다르게, HTTP 메시지 바디를 통해 데이터가 직접 넘어오는 경우는 `@RequestParam`, `@ModelAttribute`를 사용할 수 없다.**

  - 물론 HTML Form 형식으로 전달되는 경우는 요청 파라미터로 인정된다.

- 먼저 가장 단순한 텍스트 메시지를 HTTP 메시지 바디에 담아서 전송하고, 읽어보자.
- HTTP 메시지 바디의 데이터를 `InputStream`을 사용해서 직접 읽을 수 있다.

  - InputStream(Reader): HTTP 요청 메시지 바디의 내용을 직접 조회
  - OutputStream(Writer): HTTP 응답 메시지의 바디에 직접 결과 출력

- `HttpEntity`: HTTP header, body 정보를 편리하게 조회

  - 메시지 바디 정보를 직접 조회
  - 요청 파라미터를 조회하는 기능과 관계 없음 @RequestParam X, @ModelAttribute X
  - `HttpEntity`는 응답에도 사용 가능
  - 메시지 바디 정보 직접 반환
  - 헤더 정보 포함 가능
  - `HttpEntity` 를 상속받은 다음 객체들도 같은 기능을 제공한다.
    - RequestEntity - HttpMethod, url 정보가 추가, 요청에서 사용
    - ResponseEntity - HTTP 상태 코드 설정 가능, 응답에서 사용
      - `return new ResponseEntity<String>("Hello World", responseHeaders, HttpStatus.CREATED)`

- 스프링MVC 내부에서 HTTP 메시지 바디를 읽어서 문자나 객체로 변환해서 전달해주는데, 이때 HTTP 메시지컨버터(HttpMessageConverter)라는 기능을 사용한다.

#### `@RequestBody`

- `@RequestBody` 를 사용하면 HTTP 메시지 바디 정보를 편리하게 조회할 수 있다.
- 참고로 헤더 정보가 필요하다면 `HttpEntity` 를 사용하거나 `@RequestHeader`를 사용하면 된다.

요청 파라미터 vs HTTP 메시지 바디

- 요청 파라미터를 조회하는 기능: `@RequestParam`, `@ModelAttribute`
- HTTP 메시지 바디를 직접 조회하는 기능: `@RequestBody`

- `@ResponseBody`
  - `@ResponseBody`를 사용하면 응답 결과를 HTTP 메시지 바디에 직접 담아서 전달할 수 있다.
  - 물론 이 경우에도 view를 사용하지 않는다.

### 54. HTTP 요청 메시지 - JSON

- `HttpServletRequest`를 사용해서 직접 HTTP 메시지 바디에서 데이터를 읽어와서, 문자로 변환한다.
- 문자로 된 JSON 데이터를 Jackson 라이브러리인 `objectMapper` 를 사용해서 자바 객체로 변환한다.
- 문자로 된 JSON 데이터인 `messageBody` 를 `objectMapper` 를 통해서 자바 객체로 변환한다.

- `@RequestBody` 객체 파라미터
  - `@RequestBody` 에 직접 만든 객체를 지정할 수 있다.
- HttpEntity , @RequestBody 를 사용하면 HTTP 메시지 컨버터가 HTTP 메시지 바디의 내용을 우리가 원하는 문자나 객체 등으로 변환해준다.
- @RequestBody는 생략 불가능

  - **생략하면 HTTP 메시지 바디가 아니라 요청 파라미터를 처리하게 된다.**
  - 스프링은 `@ModelAttribute` , `@RequestParam`과 같은 해당 애노테이션을 생략시 다음과 같은 규칙을 적용한다.
  - String , int , Integer 같은 단순 타입 = @RequestParam
  - 나머지 = @ModelAttribute (argument resolver 로 지정해둔 타입 외)
  - **따라서 이 경우 HelloData에 `@RequestBody`를 생략하면 `@ModelAttribute`가 적용되어버린다.**

- `@ResponseBody`
  - 응답의 경우에도 `@ResponseBody`를 사용하면 해당 객체를 HTTP 메시지 바디에 직접 넣어줄 수 있다.
  - 물론 이 경우에도 `HttpEntity`를 사용해도 된다.
- @RequestBody 요청
  - JSON 요청 HTTP 메시지 컨버터 객체
- @ResponseBody 응답
  - 객체 HTTP 메시지 컨버터 JSON 응답

### 55. 응답 - 정적 리소스, 뷰 템플릿

- [ResponseViewController](./src/main/java/com/spring/mvc/basic/response/ResponseViewController.java)
- 스프링(서버)에서 응답 데이터를 만드는 방법은 크게 3가지이다.
  - 정적 리소스
    - 예) 웹 브라우저에 정적인 HTML, css, js를 제공할 때는, 정적 리소스를 사용한다.
  - 뷰 템플릿 사용
    - 예) 웹 브라우저에 동적인 HTML을 제공할 때는 뷰 템플릿을 사용한다.
  - HTTP 메시지 사용
    - HTTP API를 제공하는 경우에는 HTML이 아니라 데이터를 전달해야 하므로, HTTP 메시지 바디에
    - JSON 같은 형식으로 데이터를 실어 보낸다.

#### 정적 리소스

- 스프링 부트는 클래스패스의 다음 디렉토리에 있는 정적 리소스를 제공한다.
- /static , /public , /resources , /META-INF/resources
- `src/main/resources`는 리소스를 보관하는 곳이고, 또 클래스패스의 시작 경로이다.
- 정적 리소스는 해당 파일을 변경 없이 그대로 서비스하는 것이다.

#### 뷰 템플릿 경로

- src/main/resources/templates
- String을 반환하는 경우 - View or HTTP 메시지
  - @ResponseBody 가 없으면 response/hello 로 뷰 리졸버가 실행되어서 뷰를 찾고, 렌더링 한다.
  - 여기서는 뷰의 논리 이름인 response/hello 를 반환하면 다음 경로의 뷰 템플릿이 렌더링 되는 것을 확인할 수 있다.
- Void를 반환하는 경우
  - @Controller 를 사용하고, HttpServletResponse , OutputStream(Writer) 같은 HTTP 메시지 바디를 처리하는 파라미터가 없으면 요청 URL을 참고해서 논리 뷰 이름으로 사용
  - 참고로 이 방식은 명시성이 너무 떨어지고 이렇게 딱 맞는 경우도 많이 없어서, 권장하지 않는다.

#### HTTP 메시지

- @ResponseBody , HttpEntity 를 사용하면, 뷰 템플릿을 사용하는 것이 아니라, HTTP 메시지 바디에 직접 응답 데이터를 출력할 수 있다.

### 56. HTTP 응답 - HTTP API, 메시지 바디에 직접 입력

- ResponseEntity 엔티티는 HttpEntity 를 상속 받았는데, HttpEntity는 HTTP 메시지의 헤더, 바디 정보를 가지고 있다.

  - ResponseEntity 는 여기에 더해서 HTTP 응답 코드를 설정할 수 있다.

- @ResponseBody 를 사용하면 view를 사용하지 않고, HTTP 메시지 컨버터를 통해서 HTTP 메시지를 직접 입력할수 있다.
  - ResponseEntity 도 동일한 방식으로 동작한다.
  - @ResponseStatus(HttpStatus.OK) 애노테이션을 사용하면 응답 코드도 설정할 수 있다.

### 57. HTTP 메시지 컨버터

- @ResponseBody 를 사용

  - HTTP의 BODY에 문자 내용을 직접 반환
  - viewResolver 대신에 HttpMessageConverter 가 동작
  - 기본 문자처리: StringHttpMessageConverter
  - 기본 객체처리: MappingJackson2HttpMessageConverter
  - byte 처리 등등 기타 여러 HttpMessageConverter가 기본으로 등록되어 있음
  - 응답의 경우 클라이언트의 HTTP Accept 해더와 서버의 컨트롤러 반환 타입 정보 둘을 조합해서 HttpMessageConverter 가 선택된다.

- 스프링 MVC는 다음의 경우에 HTTP 메시지 컨버터를 적용한다.
  - HTTP 요청: @RequestBody , HttpEntity(RequestEntity)
  - HTTP 응답: @ResponseBody , HttpEntity(ResponseEntity)
- 스프링 부트는 다양한 메시지 컨버터를 제공하는데, 대상 클래스 타입과 미디어 타입 둘을 체크해서 사용여부를 결정한다. 만약 만족하지 않으면 다음 메시지 컨버터로 우선순위가 넘어간다.

- 몇가지 주요한 메시지 컨버터를 알아보자.
- ByteArrayHttpMessageConverter : byte[] 데이터를 처리한다.
  - 클래스 타입: byte[] , 미디어타입: _/_ ,
  - 요청 예) @RequestBody byte[] data
  - 응답 예) @ResponseBody return byte[] 쓰기 미디어타입 application/octet-stream
- StringHttpMessageConverter : String 문자로 데이터를 처리한다.
  - 클래스 타입: String , 미디어타입: _/_
  - 요청 예) @RequestBody String data
  - 응답 예) @ResponseBody return "ok" 쓰기 미디어타입 text/plain
- MappingJackson2HttpMessageConverter : application/json
  - 클래스 타입: 객체 또는 HashMap , 미디어타입 application/json 관련
  - 요청 예) @RequestBody HelloData data
  - 응답 예) @ResponseBody return helloData 쓰기 미디어타입 application/json 관련

#### HTTP 요청 데이터 읽기

- HTTP 요청이 오고, 컨트롤러에서 @RequestBody , HttpEntity 파라미터를 사용한다.
- 메시지 컨버터가 메시지를 읽을 수 있는지 확인하기 위해 canRead() 를 호출한다.
- 대상 클래스 타입을 지원하는가.
  - 예) @RequestBody 의 대상 클래스 ( byte[] , String , HelloData )
- HTTP 요청의 Content-Type 미디어 타입을 지원하는가.
  - 예) text/plain , application/json , _/_
- canRead() 조건을 만족하면 read() 를 호출해서 객체 생성하고, 반환한다.

#### HTTP 응답 데이터 생성

- 컨트롤러에서 @ResponseBody , HttpEntity 로 값이 반환된다.
- 메시지 컨버터가 메시지를 쓸 수 있는지 확인하기 위해 canWrite() 를 호출한다.
- 대상 클래스 타입을 지원하는가.
  - 예) return의 대상 클래스 ( byte[] , String , HelloData )
- HTTP 요청의 Accept 미디어 타입을 지원하는가.(더 정확히는 @RequestMapping 의 produces )
  - 예) text/plain , application/json , _/_
- canWrite() 조건을 만족하면 write() 를 호출해서 HTTP 응답 메시지 바디에 데이터를 생성한다.

### 58. 요청 매핑 헨들러 어뎁터 구조

#### RequestMappingHandlerAdapter 동작 방식

- 생각해보면, 애노테이션 기반의 컨트롤러는 매우 다양한 파라미터를 사용할 수 있었다.
- HttpServletRequest , Model 은 물론이고, @RequestParam , @ModelAttribute 같은 애노테이션 그리고 @RequestBody , HttpEntity 같은 HTTP 메시지를 처리하는 부분까지 매우 큰 유연함을 보여주었다.
- 이렇게 파라미터를 유연하게 처리할 수 있는 이유가 바로 `ArgumentResolver` 덕분이다.
- 애노테이션 기반 컨트롤러를 처리하는 RequestMappingHandlerAdapter 는 바로 이 ArgumentResolver 를 호출해서 컨트롤러(핸들러)가 필요로 하는 다양한 파라미터의 값(객체)을 생성한다. 그리고 이렇게 파리미터의 값이 모두 준비되면 컨트롤러를 호출하면서 값을 넘겨준다.

- 스프링은 30개가 넘는 ArgumentResolver 를 기본으로 제공한다.

1. ArgumentResolver 의 supportsParameter() 를 호출해서 해당 파라미터를 지원하는지 체크하고,
2. 지원하면 resolveArgument() 를 호출해서 실제 객체를 생성한다.
3. 그리고 이렇게 생성된 객체가 컨트롤러 호출시 넘어가는 것이다.

- 그리고 원한다면 여러분이 직접 이 인터페이스를 확장해서 원하는 ArgumentResolver 를 만들 수도 있다. 실제 확장하는 예제는 향후 로그인 처리에서 진행하겠다.

- HandlerMethodReturnValueHandler 를 줄여서 ReturnValueHandler 라 부른다.

  - ArgumentResolver 와 비슷한데, 이것은 응답 값을 변환하고 처리한다.
  - 컨트롤러에서 String으로 뷰 이름을 반환해도, 동작하는 이유가 바로 ReturnValueHandler 덕분이다.

- 스프링은 10여개가 넘는 ReturnValueHandler 를 지원한다.
  - 예) ModelAndView , @ResponseBody , HttpEntity , String

#### HTTP 메시지 컨버터는 어디쯤 있을까?

- HTTP 메시지 컨버터를 사용하는 @RequestBody 도 컨트롤러가 필요로 하는 파라미터의 값에 사용된다.
- @ResponseBody 의 경우도 컨트롤러의 반환 값을 이용한다.

- 요청의 경우 @RequestBody 를 처리하는 ArgumentResolver 가 있고, HttpEntity 를 처리하는 ArgumentResolver 가 있다.

  - 이 ArgumentResolver 들이 HTTP 메시지 컨버터를 사용해서 필요한 객체를 생성하는 것이다. (어떤 종류가 있는지 코드로 살짝 확인해보자)

- 응답의 경우 @ResponseBody 와 HttpEntity 를 처리하는 ReturnValueHandler 가 있다.

  - 그리고 여기에서 HTTP 메시지 컨버터를 호출해서 응답 결과를 만든다.

- 스프링 MVC는 @RequestBody @ResponseBody 가 있으면 RequestResponseBodyMethodProcessor(ArgumentResolver, ReturnValueHandler)
- HttpEntity 가 있으면 HttpEntityMethodProcessor(ArgumentResolver, ReturnValueHandler) 를 사용한다.

- 확장
  - 스프링은 다음을 모두 인터페이스로 제공한다. 따라서 필요하면 언제든지 기능을 확장할 수 있다.
  - HandlerMethodArgumentResolver
  - HandlerMethodReturnValueHandler
  - HttpMessageConverter
  - 기능 확장은 WebMvcConfigurer 를 상속 받아서 스프링 빈으로 등록하면 된다.
  - 스프링이 필요한 대부분의 기능을 제공하기 때문에 실제 기능을 확장할 일이 많지는 않다.

#### 스프링 MVC 요청 처리 전체 라이프사이클 🚀

사용자가 HTTP 요청을 보냈을 때부터 응답을 받기까지, 스프링 내부에서는 다음과 같은 일들이 순서대로 일어납니다.

1. 요청 접수: DispatcherServlet
   모든 요청은 가장 먼저 프론트 컨트롤러인 DispatcherServlet에 도착합니다. DispatcherServlet은 전체 흐름을 조율하는 지휘자 역할을 합니다.
2. 핸들러 탐색: HandlerMapping
   DispatcherServlet은 등록된 HandlerMapping(주로 RequestMappingHandlerMapping)에게 "이 요청을 처리할 핸들러(컨트롤러 메서드)가 누구야?"라고 물어봅니다.HandlerMapping은 @RequestMapping 등의 어노테이션 정보를 바탕으로 요청을 처리할 적절한 컨트롤러 메서드를 찾아 HandlerMethod 객체로 포장하여 반환합니다.
3. 핸들러 어댑터 선택: HandlerAdapter
   DispatcherServlet은 찾아낸 HandlerMethod를 실행할 수 있는 HandlerAdapter(주로 RequestMappingHandlerAdapter)를 선택합니다.
4. 인수(Argument) 처리: HandlerMethodArgumentResolver
   RequestMappingHandlerAdapter는 컨트롤러 메서드를 호출하기 전에, 메서드가 필요로 하는 파라미터들을 준비해야 합니다.
   이때 **HandlerMethodArgumentResolver**가 동작합니다.
   메서드 파라미터에 **@RequestBody**가 있으면, RequestResponseBodyMethodProcessor라는 ArgumentResolver가 선택됩니다.
   이 ArgumentResolver는 HttpMessageConverter(MappingJackson2HttpMessageConverter 등)를 사용해 요청 본문의 JSON 데이터를 파라미터 객체(HelloData)로 변환합니다.
   @RequestParam, Model 등 다른 파라미터들도 각각에 맞는 ArgumentResolver가 처리합니다.

5. 컨트롤러 메서드 호출
   모든 파라미터가 준비되면, RequestMappingHandlerAdapter는 준비된 값들을 가지고 실제 컨트롤러 메서드를 호출합니다.
   컨트롤러는 비즈니스 로직을 수행하고 결과 값(String, ModelAndView, 객체 등)을 반환합니다.

6. 반환 값 처리: HandlerMethodReturnValueHandler
   RequestMappingHandlerAdapter는 컨트롤러가 반환한 값을 처리하기 위해 **HandlerMethodReturnValueHandler**를 사용합니다.
   메서드에 **@ResponseBody**가 있고 객체를 반환하면, RequestResponseBodyMethodProcessor라는 ReturnValueHandler가 선택됩니다.
   이 ReturnValueHandler는 HttpMessageConverter(MappingJackson2HttpMessageConverter 등)를 사용해 반환된 객체를 JSON 문자열로 변환하여 응답 본문에 직접 써줍니다.
   **API 응답의 경우 여기서 흐름 종료**
   반환 값이 **String**이라면 ViewNameMethodReturnValueHandler가 "뷰 이름이구나!"라고 판단하고 ModelAndView 객체에 담습니다.

7. 뷰 렌더링: ViewResolver, View
   ModelAndView가 반환되면, DispatcherServlet은 **ViewResolver**를 호출합니다.
   ViewResolver는 "new-form"과 같은 논리 뷰 이름을 /WEB-INF/views/new-form.jsp와 같은 물리 경로를 가진 View 객체로 변환합니다.
   DispatcherServlet은 최종적으로 View 객체의 render() 메서드를 호출하여 JSP나 Thymeleaf 같은 템플릿 엔진을 통해 HTML 응답을 생성하고 사용자에게 반환합니다.

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
