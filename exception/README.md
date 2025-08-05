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

- HTML 페이지의 경우 지금까지 설명했던 것 처럼 4xx, 5xx와 같은 오류 페이지만 있으면 대부분의 문제를 해결할 수 있다.
- 그런데 API의 경우에는 생각할 내용이 더 많다. 오류 페이지는 단순히 고객에게 오류 화면을 보여주고 끝이지만, API는 각 오류 상황에 맞는 오류 응답 스펙을 정하고, JSON으로 데이터를 내려주어야 한다.

- API를 요청했는데, 정상의 경우 API로 JSON 형식으로 데이터가 정상 반환된다.
- 오류가 발생하면 우리가 미리 만들어둔 오류 페이지 HTML이 반환된다.
- 오류 페이지 컨트롤러도 JSON 응답을 할 수 있도록 하는 방식 - 쓸일 없음

### 103. 스프링 부트 기본 오류 처리

- API 예외 처리도 스프링 부트가 제공하는 BasicErrorController 기본 오류 방식을 사용할 수 있다.
- 스프링 부트의 기본 설정은 오류 발생시 /error 를 오류 페이지로 요청한다.
- BasicErrorController 는 이 경로를 기본으로 받는다.

- BasicErrorController 를 확장하면 JSON 메시지도 변경할 수 있다.
- 그런데 API 오류는 조금 뒤에 설명할 **@ExceptionHandler 가 제공하는 기능을 사용하는 것이 더 나은 방법**이므로 지금은 BasicErrorController를 확장해서 JSON 오류 메시지를 변경할 수 있다 정도로만 이해해두자.

- 스프링 부트가 제공하는 BasicErrorController 는 HTML 페이지를 제공하는 경우에는 매우 편리하다.
- 그런데 API 오류 처리는 다른 차원의 이야기이다.

### 104. HandlerExceptionResolver 시작

- 예외가 발생해서 서블릿을 넘어 WAS까지 예외가 전달되면 HTTP 상태코드가 500으로 처리된다.
- 오류 메시지, 형식등을 API마다 다르게 처리하고 싶다.

- 예를 들어서 IllegalArgumentException 을 처리하지 못해서 컨트롤러 밖으로 넘어가는 일이 발생하면 HTTP 상태코드를 400으로 처리하고 싶다. 어떻게 해야할까?
- 스프링 MVC는 컨트롤러(핸들러) 밖으로 예외가 던져진 경우 예외를 해결하고, 동작을 새로 정의할 수 있는 방법을 제공한다.
- 컨트롤러 밖으로 던져진 예외를 해결하고, 동작 방식을 변경하고 싶으면 HandlerExceptionResolver 를 사용하면 된다.
- 줄여서 ExceptionResolver라 한다.

  - ExceptionResolver 가 ModelAndView 를 반환하는 이유는 마치 try, catch를 하듯이, Exception 을 처리해서 정상 흐름 처럼 변경하는 것이 목적이다.
  - 이름 그대로 Exception 을 Resolver(해결)하는 것이 목적이다.

- 여기서는 IllegalArgumentException 이 발생하면 response.sendError(400) 를 호출해서 HTTP 상태코드를 400으로 지정하고, 빈 ModelAndView 를 반환한다.

- HandlerExceptionResolver 의 반환 값에 따른 DispatcherServlet 의 동작 방식은 다음과 같다.

  - 빈 ModelAndView: new ModelAndView() 처럼 빈 ModelAndView 를 반환하면 뷰를 렌더링 하지 않고, 정상 흐름으로 서블릿이 리턴된다.
  - ModelAndView 지정: ModelAndView 에 View , Model 등의 정보를 지정해서 반환하면 뷰를 렌더링 한다.
  - null: null 을 반환하면, 다음 ExceptionResolver 를 찾아서 실행한다. 만약 처리할 수 있는 ExceptionResolver 가 없으면 예외 처리가 안되고, 기존에 발생한 예외를 서블릿 밖으로 던진다.

- 예외 상태 코드 변환
  - 예외를 response.sendError(xxx) 호출로 변경해서 서블릿에서 상태 코드에 따른 오류를 처리하도록 위임
  - 이후 WAS는 서블릿 오류 페이지를 찾아서 내부 호출,
  - 예를 들어서 스프링 부트가 기본으로 설정한 /error 가 호출됨
- 뷰 템플릿 처리
  - ModelAndView 에 값을 채워서 예외에 따른 새로운 오류 화면 뷰 렌더링 해서 고객에게 제공가능
- API 응답 처리

  - response.getWriter().println("hello"); 처럼 HTTP 응답 바디에 직접 데이터를 넣어주는 것도 가능하다.
  - 여기에 JSON 으로 응답하면 API 응답 처리를 할 수 있다.

- **configureHandlerExceptionResolvers(..) 를 사용하면 스프링이 기본으로 등록하는 ExceptionResolver 가 제거되므로 주의, extendHandlerExceptionResolvers 를 사용하자.**

### 105. HandlerExceptionResolver 활용

- ExceptionResolver 를 사용하면 컨트롤러에서 예외가 발생해도 ExceptionResolver 에서 예외를 처리해버린다.
- 따라서 예외가 발생해도 서블릿 컨테이너까지 예외가 전달되지 않고, 스프링 MVC에서 예외 처리는 끝이 난다.
- 결과적으로 WAS 입장에서는 정상 처리가 된 것이다. 이렇게 예외를 이곳에서 모두 처리할 수 있다는 것이 핵심이다.
- 서블릿 컨테이너까지 예외가 올라가면 복잡하고 지저분하게 추가 프로세스가 실행된다.
- 반면에 ExceptionResolver 를 사용하면 예외처리가 상당히 깔끔해진다.
- 그런데 직접 ExceptionResolver 를 구현하려고 하니 상당히 복잡하다.

### 106. 스프링이 제공하는 ExceptionResolver1

- 스프링 부트가 기본으로 제공하는 ExceptionResolver 는 다음과 같다.
- HandlerExceptionResolverComposite 에 다음 순서로 등록

  1. ExceptionHandlerExceptionResolver - @ExceptionHandler 을 처리한다. API 예외 처리는 대부분 이 기능으로 해결한다.
  2. ResponseStatusExceptionResolver - HTTP 상태 코드를 지정해준다.
  3. DefaultHandlerExceptionResolver - 내부 기본 예외를 처리한다. 우선 순위가 가장 낮다.

- ResponseStatusExceptionResolver 는 예외에 따라서 HTTP 상태 코드를 지정해주는 역할을 한다.
  - 다음 두 가지 경우를 처리한다.
  - @ResponseStatus 가 달려있는 예외
    - @ResponseStatus 는 개발자가 직접 변경할 수 없는 예외에는 적용할 수 없다.
    - 애노테이션을 직접 넣어야 하는데, 내가 코드를 수정할 수 없는 라이브러리의 예외 코드 같은 곳에는 적용할 수 없다.
    - 추가로 애노테이션을 사용하기 때문에 조건에 따라 동적으로 변경하는 것도 어렵다.
  - ResponseStatusException 예외

### 107. 스프링이 제공하는 ExceptionResolver2

- DefaultHandlerExceptionResolver 는 스프링 내부에서 발생하는 스프링 예외를 해결한다.
- 대표적으로 파라미터 바인딩 시점에 타입이 맞지 않으면 내부에서 TypeMismatchException 이 발생하는데,
- 이 경우 예외가 발생했기 때문에 그냥 두면 서블릿 컨테이너까지 오류가 올라가고, 결과적으로 500 오류가 발생한다.
- 그런데 파라미터 바인딩은 대부분 클라이언트가 HTTP 요청 정보를 잘못 호출해서 발생하는 문제이다.
- HTTP 에서는 이런 경우 HTTP 상태 코드 400을 사용하도록 되어 있다.
- DefaultHandlerExceptionResolver 는 이것을 500 오류가 아니라 HTTP 상태 코드 400 오류로 변경한다.
- 스프링 내부 오류를 어떻게 처리할지 수 많은 내용이 정의되어 있다.
  - response.sendError(HttpServletResponse.SC_BAD_REQUEST) (400)
  - 결국 response.sendError() 를 통해서 문제를 해결한다.

### 108. @ExceptionHandler

- HandlerExceptionResolver 를 떠올려 보면 ModelAndView 를 반환해야 했다. 이것은 API 응답에는 필요하지 않다.
- API 응답을 위해서 HttpServletResponse 에 직접 응답 데이터를 넣어주었다. 이것은 매우 불편하다.
  - 스프링 컨트롤러에 비유하면 마치 과거 서블릿을 사용하던 시절로 돌아간 것 같다.
- 특정 컨트롤러에서만 발생하는 예외를 별도로 처리하기 어렵다.

  - 예를 들어서 회원을 처리하는 컨트롤러에서 발생하는 RuntimeException 예외와
  - 상품을 관리하는 컨트롤러에서 발생하는 동일한 RuntimeException 예외를 서로 다른 방식으로 처리하려면?

- 스프링은 API 예외 처리 문제를 해결하기 위해 @ExceptionHandler 라는 애노테이션을 사용하는 매우 편리한 예외처리 기능을 제공하는데, 이것이 바로 ExceptionHandlerExceptionResolver 이다.
- 스프링은 ExceptionHandlerExceptionResolver 를 기본으로 제공하고,
- 기본으로 제공하는 ExceptionResolver 중에 우선순위도 가장 높다.
- **실무에서 API 예외 처리는 대부분 이 기능을 사용한다.**

- @ExceptionHandler 애노테이션을 선언하고, 해당 컨트롤러에서 처리하고 싶은 예외를 지정해주면 된다.
- 해당 컨트롤러에서 예외가 발생하면 이 메서드가 호출된다.
  - 참고로 지정한 예외 또는 그 예외의 자식 클래스는 모두 잡을 수 있다.
- 스프링의 우선순위는 항상 자세한 것이 우선권을 가진다.
- @ExceptionHandler 에 지정한 부모 클래스는 자식 클래스까지 처리할 수 있다.

- 컨트롤러를 호출한 결과 IllegalArgumentException 예외가 컨트롤러 밖으로 던져진다.
- 예외가 발생했으로 ExceptionResolver 가 작동한다.
- 가장 우선순위가 높은ExceptionHandlerExceptionResolver 가 실행된다.
- ExceptionHandlerExceptionResolver 는 해당 컨트롤러에 IllegalArgumentException 을 처리할 수 있는 @ExceptionHandler 가 있는지 확인한다.
- illegalExHandle() 를 실행한다.
- @RestController 이므로 illegalExHandle() 에도 @ResponseBody 가 적용된다.
- 따라서 HTTP 컨버터가 사용되고, 응답이 다음과 같은 JSON으로 반환된다.
- @ResponseStatus(HttpStatus.BAD_REQUEST) 를 지정했으므로 HTTP 상태 코드 400으로 응답한다.

### 109. @ControllerAdvice

- @ExceptionHandler 를 사용해서 예외를 깔끔하게 처리할 수 있게 되었지만, 정상 코드와 예외 처리 코드가 하나의 컨트롤러에 섞여 있다.
- @ControllerAdvice 또는 @RestControllerAdvice 를 사용하면 둘을 분리할 수 있다.

- @ControllerAdvice 는 대상으로 지정한 여러 컨트롤러에 @ExceptionHandler , @InitBinder 기능 을 부여해주는 역할을 한다.
- @ControllerAdvice 에 대상을 지정하지 않으면 모든 컨트롤러에 적용된다. (글로벌 적용)
- @RestControllerAdvice 는 @ControllerAdvice 와 같고, @ResponseBody 가 추가되어 있다.

### 110. 정리
