## 16. 예외 처리와 오류 페이지

### 93. 프로젝트 생성

### 94. 서블릿 예외 처리 - 시작

- 자바의 메인 메서드를 직접 실행하는 경우 main 이라는 이름의 쓰레드가 실행된다.
- 실행 도중에 예외를 잡지 못하고 처음 실행한 main() 메서드를 넘어서 예외가 던져지면, 예외 정보를 남기고 해당 쓰레드는 종료된다.

- 웹 애플리케이션은 사용자 요청별로 별도의 쓰레드가 할당되고, 서블릿 컨테이너 안에서 실행된다.
- 애플리케이션에서 예외가 발생했는데, 어디선가 try ~ catch로 예외를 잡아서 처리하면 아무런 문제가 없다.
- 그런데 만약에 애플리케이션에서 예외를 잡지 못하고, 서블릿 밖으로 까지 예외가 전달되면 어떻게 동작할까?
- `WAS(여기까지 전파) <- 필터 <- 서블릿 <- 인터셉터 <- 컨트롤러(예외발생)`

- 결국 톰캣 같은 WAS 까지 예외가 전달된다. WAS는 예외가 올라오면 어떻게 처리해야 할까?

  - 실행해보면 다음처럼 tomcat이 기본으로 제공하는 오류 화면을 볼 수 있다.
  - Exception 의 경우 서버 내부에서 처리할 수 없는 오류가 발생한 것으로 생각해서 HTTP 상태 코드 500을 반환한다.
  - 이 경우 Node 계열은 서버가 다운되는 것으로 기억한다.

- 사용자 요청이 들어올 때마다, 스레드 풀에서 스레드 하나를 할당하여 그 요청을 처리하게 합니다.
- 만약 특정 요청을 처리하던 스레드에서 해결할 수 없는 에러(예외)가 발생하면, 스프링의 DispatcherServlet이 최종적으로 이 예외를 잡아 HTTP 500 응답을 보냅니다.
- 에러가 난 해당 스레드만 종료되고, 다른 스레드에서 처리 중인 다른 요청들은 아무런 영향을 받지 않습니다.
- 서버 전체 프로세스는 계속 정상적으로 동작합니다.

### 95. 서블릿 예외 처리 - 오류 화면 제공

- 스프링 부트를 통해서 서블릿 컨테이너를 실행하기 때문에, 스프링 부트가 제공하는 기능을 사용해서 서블릿 오류 페이지를 등록하면 된다.

### 96. 서블릿 예외 처리 - 오류 페이지 작동 원리

- 서블릿은 Exception (예외)가 발생해서 서블릿 밖으로 전달되거나 또는 response.sendError() 가 호출 되었을 때 설정된 오류 페이지를 찾는다.
  - 예외 발생 흐름 - `WAS(여기까지 전파) <- 필터 <- 서블릿 <- 인터셉터 <- 컨트롤러(예외발생)`
  - sendError 흐름 - `WAS(sendError 호출 기록 확인) <- 필터 <- 서블릿 <- 인터셉터 <- 컨트롤러(response.sendError())`
- WAS는 해당 예외를 처리하는 오류 페이지 정보를 확인한다.
  - new ErrorPage(RuntimeException.class, "/error-page/500")
  - 오류 페이지 요청 흐름 `WAS "/error-page/500" 다시 요청 -> 필터 -> 서블릿 -> 인터셉터 -> 컨트롤러(/error-page/500)-> View`
- 예외 발생과 오류 페이지 요청 흐름

  1. WAS(여기까지 전파) <- 필터 <- 서블릿 <- 인터셉터 <- 컨트롤러(예외발생)
  2. WAS `/error-page/500` 다시 요청 -> 필터 -> 서블릿 -> 인터셉터 -> 컨트롤러 -> View

- 오직 서버 내부에서 오류 페이지를 찾기 위해 추가적인 호출을 한다.

1. 예외가 발생해서 WAS까지 전파된다.
2. WAS는 오류 페이지 경로를 찾아서 내부에서 오류 페이지를 호출한다. 이때 오류 페이지 경로로 필터, 서블릿, 인터셉터, 컨트롤러가 모두 다시 호출된다.

- `javax.servlet.error.exception` : 예외
- `javax.servlet.error.exception_type` : 예외 타입
- `javax.servlet.error.message` : 오류 메시지
- `javax.servlet.error.request_uri` : 클라이언트 요청 URI
- `javax.servlet.error.servlet_name` : 오류가 발생한 서블릿 이름
- `javax.servlet.error.status_code` : HTTP 상태 코드

### 97. 서블릿 예외 처리 - 필터

- 오류가 발생하면 오류 페이지를 출력하기 위해 WAS 내부에서 다시 한번 호출이 발생한다.
- 이때 필터, 서블릿, 인터셉터도 모두 다시 호출된다.
- 그런데 로그인 인증 체크 같은 경우를 생각해보면, 이미 한번 필터나, 인터셉터에서 로그인 체크를 완료했다.
- 따라서 서버 내부에서 오류 페이지를 호출한다고 해서 해당 필터나 인터셉트가 한번 더 호출되는 것은 매우 비효율적이다.
- 결국 클라이언트로 부터 발생한 정상 요청인지, 아니면 오류 페이지를 출력하기 위한 내부 요청인지 구분할 수 있어야한다.
- **서블릿은 이런 문제를 해결하기 위해 DispatcherType 이라는 추가 정보를 제공한다.**

- `REQUEST` : 클라이언트 요청
- `ERROR` : 오류 요청
- `FORWARD` : MVC에서 배웠던 서블릿에서 다른 서블릿이나 JSP를 호출할 때 RequestDispatcher.forward(request, response);
- `INCLUDE` : 서블릿에서 다른 서블릿이나 JSP의 결과를 포함할 때 RequestDispatcher.include(request, response);
- `ASYNC` : 서블릿 비동기 호출

- 필터 등록 시 기본 값이 DispatcherType.REQUEST 이다. 즉 클라이언트의 요청이 있는 경우에만 필터가 적용된다.
  - 특별히 오류 페이지 경로도 필터를 적용할 것이 아니면, 기본 값을 그대로 사용하면 된다.
  - 물론 오류 페이지 요청 전용 필터를 적용하고 싶으면 DispatcherType.ERROR 만 지정하면 된다.

### 98. 서블릿 예외 처리 - 인터셉터

- 필터의 경우에는 필터를 등록할 때 어떤 DispatcherType인 경우에 필터를 적용할 지 선택할 수 있었다.
- 그런데 인터셉터는 서블릿이 제공하는 기능이 아니라 스프링이 제공하는 기능이다.
  - 따라서 DispatcherType 과 무관하게 항상 호출된다.
- 인터셉터는 다음과 같이 요청 경로에 따라서 추가하거나 제외하기 쉽게 되어 있기 때문에,
- 이러한 설정을 사용해서 오류 페이지 경로를 excludePathPatterns 를 사용해서 빼주면 된다.

- 전체 흐름 정리
- /hello 정상 요청 `WAS(/hello, dispatchType=REQUEST) -> 필터 -> 서블릿 -> 인터셉터 -> 컨트롤러 -> View `
- /error-ex 오류 요청
  - 필터는 DispatchType 으로 중복 호출 제거 ( dispatchType=REQUEST )
  - 인터셉터는 경로 정보로 중복 호출 제거( excludePathPatterns("/error-page/\*\*") )
  1. WAS(/error-ex, dispatchType=REQUEST) -> 필터 -> 서블릿 -> 인터셉터 -> 컨트롤러
  2. WAS(여기까지 전파) <- 필터 <- 서블릿 <- 인터셉터 <- 컨트롤러(예외발생)
  3. WAS 오류 페이지 확인
  4. WAS(/error-page/500, dispatchType=ERROR) -> 필터(x) -> 서블릿 -> 인터셉터(x) -> 컨트롤러(/error-page/500) -> View

### 99. 스프링 부트 - 오류 페이지1

- 지금까지 예외 처리 페이지를 만들기 위해서 다음과 같은 복잡한 과정을 거쳤다.

  1. WebServerCustomizer 를 만들고
  2. 예외 종류에 따라서 ErrorPage 를 추가하고
  3. 예외 처리용 컨트롤러 ErrorPageController 를 만듬

- 스프링 부트는 이런 과정을 모두 기본으로 제공한다.
- ErrorPage를 자동으로 등록한다. 이때 /error 라는 경로로 기본 오류 페이지를 설정한다.
- new ErrorPage("/error") , 상태코드와 예외를 설정하지 않으면 기본 오류 페이지로 사용된다.
- 서블릿 밖으로 예외가 발생하거나, response.sendError(...) 가 호출되면 모든 오류는 /error를 호출하게 된다.
- BasicErrorController 라는 스프링 컨트롤러를 자동으로 등록한다.
  - ErrorPage 에서 등록한 /error 를 매핑해서 처리하는 컨트롤러다.
  - BasicErrorController 는 기본적인 로직이 모두 개발되어 있다.
- 개발자는 오류 페이지 화면만 BasicErrorController 가 제공하는 룰과 우선순위에 따라서 등록하면 된다.
- BasicErrorController 의 처리 순서

1. 뷰 템플릿
   resources/templates/error/500.html
   resources/templates/error/5xx.html
2. 정적 리소스( static , public )
   resources/static/error/400.html
   resources/static/error/404.html
   resources/static/error/4xx.html
3. 적용 대상이 없을 때 뷰 이름( error )
   resources/templates/error.html

- 해당 경로 위치에 HTTP 상태 코드 이름의 뷰 파일을 넣어두면 된다.
- 뷰 템플릿이 정적 리소스보다 우선순위가 높고, 404, 500처럼 구체적인 것이 5xx처럼 덜 구체적인 것 보다 우선순위가 높다.

### 100. 스프링 부트 - 오류 페이지2

- BasicErrorController 컨트롤러는 다음 정보를 model에 담아서 뷰에 전달한다.
- 뷰 템플릿은 이 값을 활용해서 출력할 수 있다.

  - timestamp: Fri Feb 05 00:00:00 KST 2021
  - status: 400
  - error: Bad Request
  - exception: org.springframework.validation.BindException
  - trace: 예외 trace
  - message: Validation failed for object='data'. Error count:1
  - errors: Errors(BindingResult)
  - path: 클라이언트 요청 경로 (`/hello`)

- 실무에서는 이것들을 노출하면 안된다!
- 사용자에게는 이쁜 오류 화면과 고객이 이해할 수 있는 간단한 오류 메시지를 보여주고 오류는 서버에 로그로 남겨서 로그로 확인해야 한다.
- 스프링 부트가 기본으로 제공하는 오류 페이지를 활용하면 오류 페이지와 관련된 대부분의 문제는 손쉽게 해결할 수 있다.

### 101. 정리

## 17. API 예외 처리

### 102. 시작

### 103. 스프링 부트 기본 오류 처리

### 104. HandlerExceptionResolver 시작

### 105. HandlerExceptionResolver 활용

### 106. 스프링이 제공하는 ExceptionResolver1

### 107. 스프링이 제공하는 ExceptionResolver2

### 108. @ExceptionHandler

### 109. @ControllerAdvice

### 110. 정리

````

```

```
````
