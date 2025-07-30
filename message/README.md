# [스프링 MVC 2편 - 백엔드 웹 개발 활용 기술](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-2/dashboard)

## 11. 메시지, 국제화

### 33. 프로젝트 설정

### 34. 메시지, 국제화 소개

- 여러 화면에 보이는 상품명, 가격, 수량 등, label 에 있는 단어를 변경하려면 다음 화면들을 다 찾아가면서 모두 변경 해야 한다.
- 지금처럼 화면 수가 적으면 문제가 되지 않지만 화면이 수십개 이상이라면 수십개의 파일을 모두 고쳐야 한다.
- **다양한 메시지를 한 곳에서 관리하도록 하는 기능을 메시지 기능이라 한다.**
- 메시지에서 설명한 메시지 파일( messages.properties )을 **각 나라별로 별도로 관리하면 서비스를 국제화** 할 수 있다.

- 한국에서 접근한 것인지 영어에서 접근한 것인지는 인식하는 방법은

  - HTTP accept-language 해더 값을 사용
  - 사용자가 직접 언어를 선택하도록 하고, 쿠키 등을 사용해서 처리

- 메시지와 국제화 기능을 직접 구현할 수도 있겠지만, 스프링은 기본적인 메시지와 국제화 기능을 모두 제공한다.
- 타임리프도 스프링이 제공하는 메시지와 국제화 기능을 편리하게 통합해서 제공한다.

### 35. 스프링 메시지 소스 설정

- 메시지 관리 기능을 사용하려면 스프링이 제공하는 MessageSource 를 스프링 빈으로 등록하면 되는데,MessageSource 는 인터페이스이다.
- 따라서 구현체인 ResourceBundleMessageSource 를 스프링 빈으로 등록하면 된다.
- **스프링 부트를 사용하면 스프링 부트가 MessageSource 를 자동으로 스프링 빈으로 등록한다.**

spring.messages.basename=messages

MessageSource 를 스프링 빈으로 등록하지 않고, 스프링 부트와 관련된 별도의 설정을 하지 않으면 messages 라
는 이름으로 기본 등록된다. 따라서 messages_en.properties , messages_ko.properties ,
messages.properties 파일만 등록하면 자동으로 인식된다.

#### 테스트를 안되는 상황 발생...

1. java 17이 현재 프로젝트와 호환되지 않아 그런것 같아 vscode에서 11버전 사용하도록 설정
2. vscode 익스텐션 테스트러너가 프로젝트의 테스트를 실행하지 못함 - 버전 차이로 추정
3. 터미널에서 11로 변경 후 커맨드로 테스트를 실행
   - `./gradlew test --tests hello.itemservice.message.MessageSourceTest; open build/reports/tests/test/index.html`
4. messages.properties에서 "안녕"을 못읽음
   1. 파일을 못찾아 코드를 반환하나 확인
   2. properties 파일은 기본적으로 ASCII 기반으로 동작하므로, "안녕"과 같은 한글이 포함될 경우 인코딩 설정이 필요 하다함
      - 이 문제 아님
5. 테스트하다보니 `messages_en.properties`를 먼저 읽는 것으로 확인
   1. 시스템 언어설정이 en이어서 그런듯
   2. `Locale.setDefault(Locale.ROOT);` 추가
   3. 이부분은 가이드라인 하단에 나와있는데, 삽질해서 아쉽다...

### 36. 스프링 메시지 소스 사용

- 타임리프의 메시지 표현식 `#{...}` 를 사용하면 스프링의 메시지를 편리하게 조회할 수 있다.
- 참고로 파라미터는 다음과 같이 사용할 수 있다.
  - `hello.name=안녕 {0}`
  - `<p th:text="#{hello.name(${item.itemName})}"></p>`
- `addForm`만 변경해봄

### 37. 웹 애플리케이션에 메시지 적용하기

### 38. 웹 애플리케이션에 국제화 적용하기

- 스프링은 Locale 선택 방식을 변경할 수 있도록 LocaleResolver 라는 인터페이스를 제공하는데,
- 스프링 부트는 기본으로 Accept-Language 를 활용하는 `AcceptHeaderLocaleResolver` 를 사용한다.

### 39. 정리
