## 12. 검증1 - Validation

### 40. 검증 요구사항

- **웹 서비스는 폼 입력시 오류가 발생하면, 고객이 입력한 데이터를 유지한 상태로 어떤 오류가 발생했는지 친절하게 알려주어야 한다.**
- **컨트롤러의 중요한 역할중 하나는 HTTP 요청이 정상인지 검증하는 것이다.**

- 클라이언트 검증은 조작할 수 있으므로 보안에 취약하다.
- 서버만으로 검증하면, 즉각적인 고객 사용성이 부족해진다.
- **최종적으로 서버 검증은 필수**
- API 방식을 사용하면 API 스펙을 잘 정의해서 검증 오류를 API 응답 결과에 잘 남겨주어야 함

### 41. 프로젝트 설정 V1

### 42. 검증 직접 처리 - 소개

### 43. 검증 직접 처리 - 개발

- 입력값을 검증해 에러가 있다면 맵에 담아서 모델로 전달

- errors?. 은 errors 가 null 일때 NullPointerException 이 발생하는 대신, null 을 반환하는 문법이다.
  - th:if 에서 null 은 실패로 처리되므로 오류 메시지가 출력되지 않는다.
  - 이것은 스프링의 SpringEL이 제공하는 문법이다.
  - 내눈엔 옵셔널체이닝으로 보임

#### 남은 문제점

- 뷰 템플릿에서 중복 처리가 많다. 뭔가 비슷하다.
- 타입 오류 처리가 안된다. Item 의 price , quantity 같은 숫자 필드는 타입이 Integer 이므로 문자 타입으로 설정하는 것이 불가능하다.
  - 숫자 타입에 문자가 들어오면 오류가 발생한다. 그런데 이러한 오류는 스프링 MVC에서 컨트롤러에 진입하기도 전에 예외가 발생하기 때문에, 컨트롤러가 호출되지도 않고, 400 예외가 발생하면서 오류 페이지를 띄워준다.
- Item 의 price 에 문자를 입력하는 것 처럼 타입 오류가 발생해도 고객이 입력한 문자를 화면에 남겨야 한다.
  - 만약 컨트롤러가 호출된다고 가정해도 Item 의 price 는 Integer 이므로 문자를 보관할 수가 없다. 결국 문자는 바인딩이 불가능하므로 고객이 입력한 문자가 사라지게 되고, 고객은 본인이 어떤 내용을 입력해서 오류가 발생했는지 이해하기 어렵다.
  - 결국 고객이 입력한 값도 어딘가에 별도로 관리가 되어야 한다.

### 44. 프로젝트 준비 V2

### 45. BindingResult1

- DTO를 발리데이션하는 내용이 아니여서 아쉽다...
- 타임리프는 스프링의 `BindingResult` 를 활용해서 편리하게 검증 오류를 표현하는 기능을 제공한다.
- `#fields` : `#fields` 로 `BindingResult` 가 제공하는 검증 오류에 접근할 수 있다.
- `th:errors` : 해당 필드에 오류가 있는 경우에 태그를 출력한다. `th:if` 의 편의 버전이다.
- `th:errorclass` : `th:field` 에서 지정한 필드에 오류가 있으면 class 정보를 추가한다.

### 46. BindingResult2

- 스프링이 제공하는 검증 오류를 보관하는 객체이다.
- BindingResult 가 있으면 @ModelAttribute 에 데이터 바인딩 시 오류가 발생해도 컨트롤러가 호출된다!

  - 예) @ModelAttribute에 바인딩 시 타입 오류가 발생하면?
  - BindingResult가 없으면 400 오류가 발생하면서 컨트롤러가 호출되지 않고, 오류 페이지로 이동하지만 있으면 오류 정보(FieldError)를 BindingResult 에 담아서 컨트롤러를 정상 호출한다.

- BindingResult에 검증 오류를 적용하는 3가지 방법

  - @ModelAttribute 의 객체에 타입 오류 등으로 바인딩이 실패하는 경우 스프링이 FieldError 생성해서 BindingResult 에 넣어준다.
  - 개발자가 직접 넣어준다.
  - Validator 사용

- BindingResult 는 인터페이스이고, Errors 인터페이스를 상속받고 있다.
- 주로 관례상 BindingResult 를 많이 사용한다.

### 47. FieldError, ObjectError

- `FieldError` 는 두 가지 생성자를 제공한다.

  - `objectName` : 오류가 발생한 객체 이름
  - `field` : 오류 필드
  - `rejectedValue` : 사용자가 입력한 값(거절된 값)
  - `bindingFailure` : 타입 오류 같은 바인딩 실패인지, 검증 실패인지 구분 값
  - `codes` : 메시지 코드
  - `arguments` : 메시지에서 사용하는 인자
  - `defaultMessage` : 기본 오류 메시지

- 사용자의 입력 데이터가 컨트롤러의 @ModelAttribute 에 바인딩되는 시점에 오류가 발생하면 모델 객체에 사용자 입력 값을 유지하기 어렵다.
  - 예를 들어서 가격에 숫자가 아닌 문자가 입력된다면 가격은 Integer 타입이므로 문자를 보관할 수 있는 방법이 없다.
  - 그래서 오류가 발생한 경우 사용자 입력 값을 보관하는 별도의 방법이 필요하다.
  - 그리고 이렇게 보관한 사용자 입력 값을 검증 오류 발생시 화면에 다시 출력하면 된다.
- FieldError 는 오류 발생시 사용자 입력 값을 저장하는 기능을 제공한다.

  - rejectedValue 가 바로 오류 발생시 사용자 입력 값을 저장하는 필드다.
  - bindingFailure 는 타입 오류 같은 바인딩이 실패했는지 여부를 적어주면 된다.

- 타임리프의 사용자 입력 값 유지

  - `th:field="*{price}"`
  - 정상 상황에는 모델 객체의 값을 사용하지만, 오류가 발생하면 FieldError 에서 보관한 값을 사용해서 값을 출력

- 스프링의 바인딩 오류 처리
  - 타입 오류로 바인딩에 실패하면 스프링은 FieldError 를 생성하면서 사용자가 입력한 값을 넣어둔다. 그리고 해당오류를 BindingResult 에 담아서 컨트롤러를 호출한다.

### 48. 오류 코드와 메시지 처리1

- 에러 메세지 파일 만들고, 컨트롤러에서 에러응답에 사용
- codes : required.item.itemName 를 사용해서 메시지 코드를 지정한다. 메시지 코드는 하나가 아니라 배열로 여러 값을 전달할 수 있는데, 순서대로 매칭해서 처음 매칭되는 메시지가 사용된다.
- arguments : Object[]{1000, 1000000} 를 사용해서 코드의 {0} , {1} 로 치환할 값을 전달한다.

### 49. 오류 코드와 메시지 처리2

- FieldError , ObjectError 는 다루기 너무 번거롭다.
  - 오류 코드도 좀 더 자동화 할 수 있지 않을까? 예) item.itemName 처럼?
- 컨트롤러에서 BindingResult 는 검증해야 할 객체인 target 바로 다음에 온다.

  - 따라서 BindingResult 는 이미 본인이 검증해야 할 객체인 target 을 알고 있다.

- rejectValue() , reject() 를 사용해서 FieldError , ObjectError 를 직접 생성하지 않고, 깔끔하게 검증 오류를 다룰 수 있다.

### 50. 오류 코드와 메시지 처리3

- 단순하게 만들면 범용성이 좋아서 여러곳에서 사용할 수 있지만, 메시지를 세밀하게 작성하기 어렵다.
- 반대로 너무 자세하게 만들면 범용성이 떨어진다. 가장 좋은 방법은 범용성으로 사용하다가, 세밀하게 작성해야 하는 경우에는 세밀한 내용이 적용되도록 메시지에 단계를 두는 방법이다.

- 오류 메시지에 required.item.itemName 와 같이 객체명과 필드명을 조합한 세밀한 메시지 코드가 있으면 높은 우선순위로 사용하는 것이다.

### 51. 오류 코드와 메시지 처리4

- MessageCodesResolver: 검증 오류 코드로 메시지 코드들을 생성한다.
- MessageCodesResolver 인터페이스이고 DefaultMessageCodesResolver 는 기본 구현체이다.

- DefaultMessageCodesResolver의 기본 메시지 생성 규칙
  - 객체 오류의 경우 다음 순서로 2가지 생성
    - 1.: code + "." + object name
    - 2.: code
    - 예) 오류 코드: required, object name: item
    - 1.: required.item
    - 2.: required
  - 필드 오류의 경우 다음 순서로 4가지 메시지 코드 생성
    - 1.: code + "." + object name + "." + field
    - 2.: code + "." + field
    - 3.: code + "." + field type
    - 4.: code
    - 예) 오류 코드: typeMismatch, object name "user", field "age", field type: int
    - 1. "typeMismatch.user.age"
    - 2. "typeMismatch.age"
    - 3. "typeMismatch.int"
    - 4. "typeMismatch"

### 52. 오류 코드와 메시지 처리5

- **모든 오류 코드에 대해서 메시지를 각각 다 정의하면 개발자 입장에서 관리하기 너무 힘들다.**

  - 이거는 번역팩 텍스트, 에러메세지등 실무경험 해보면 아주 체감하는 내용

- itemName 의 경우 required 검증 오류 메시지가 발생하면 다음 코드 순서대로 메시지가 생성된다.

  1. required.item.itemName
  2. required.itemName
  3. required.java.lang.String
  4. required

- 생성된 메시지 코드를 기반으로 순서대로 MessageSource 에서 메시지에서 찾는다.
- 구체적인 것에서 덜 구체적인 순서대로 찾는다. 메시지에 1번이 없으면 2번을 찾고, 2번이 없으면 3번을 찾는다.

1. rejectValue() 호출
2. MessageCodesResolver 를 사용해서 검증 오류 코드로 메시지 코드들을 생성
3. new FieldError() 를 생성하면서 메시지 코드들을 보관
4. th:erros 에서 메시지 코드들로 메시지를 순서대로 메시지에서 찾고, 노출

### 53. 오류 코드와 메시지 처리6

- 검증 오류 코드는 다음과 같이 2가지로 나눌 수 있다.
  - 개발자가 직접 설정한 오류 코드 rejectValue() 를 직접 호출
  - 스프링이 직접 검증 오류에 추가한 경우(주로 타입 정보가 맞지 않음)
    - 타입 오류가 발생하면 typeMismatch 라는 오류 코드를 사용한다.

### 54. Validator 분리1

- 복잡한 검증 로직을 별도로 분리하자.
  - 이거는 당연한건데 스프링이 제공하는 인터페이스를 사용하는게 유용할까?
    - 검증로직은 회바회일 것 같고 해당 인터페이스를 사용하면 검증로직마다 클래스가 생길것 같다.
  - 스프링은 검증을 체계적으로 제공하기 위해 다음 인터페이스를 제공한다.
    - `public interface Validator`
    - supports() {} : 해당 검증기를 지원하는 여부 확인(뒤에서 설명)
    - validate(Object target, Errors errors) : 검증 대상 객체, BindingResult

### 55. Validator 분리2

- `Validator` 인터페이스를 사용해서 검증기를 만들면 스프링의 추가적인 도움을 받을 수 있다.
- controller 기준으로 `@Validated` 붙은 객체에 대해 자동 검증
  - `@Valid`(표준 자바) 또는 `@Validated`(Spring) 어노테이션을 사용하면 Spring이 WebDataBinder에 등록된 `Validator`를 찾아 해당 객체에 대한 검증을 자동으로 수행
- 이 기능은 DTO에 쓸 수 있을 것 같다.

```java
@InitBinder
public void init(WebDataBinder binder){
    binder.addValidators(itemValidator);
}
```

- @Validated 는 검증기를 실행하라는 애노테이션이다.
- 이 애노테이션이 붙으면 앞서 WebDataBinder 에 등록한 검증기를 찾아서 실행한다.
- 그런데 여러 검증기를 등록한다면 그 중에 어떤 검증기가 실행되어야 할지 구분이 필요하다. 이때 supports() 가 사용된다.
- 여기서는 supports(Item.class) 호출되고, 결과가 true 이므로 ItemValidator 의 validate() 가 호출된다.
- 글로벌 설정도 가능하지만 드물다.

### 56. 정리

## 13. 검증2 - Bean Validation

### 57. Bean Validation - 소개

- Bean Validation은 특정한 구현체가 아니라 Bean Validation 2.0(JSR-380)이라는 기술 표준이다.
- 쉽게 이야기해서 검증 애노테이션과 여러 인터페이스의 모음이다.
- 마치 JPA가 표준 기술이고 그 구현체로 하이버네이트가 있는 것과 같다.

- Bean Validation을 구현한 기술중에 일반적으로 사용하는 구현체는 하이버네이트 Validator이다.
- 이름이 하이버네이트가 붙어서 그렇지 ORM과는 관련이 없다.

- javax.validation 으로 시작하면 특정 구현에 관계없이 제공되는 표준 인터페이스이고,
- org.hibernate.validator 로 시작하면 하이버네이트 validator 구현체를 사용할 때만 제공되는 검증 기능이다.
- 실무에서 대부분 하이버네이트 validator를 사용하므로 자유롭게 사용해도 된다.

### 58. Bean Validation - 시작

### 59. Bean Validation - 프로젝트 준비 V3

### 60. Bean Validation - 스프링 적용

### 61. Bean Validation - 에러 코드

### 62. Bean Validation - 오브젝트 오류

### 63. Bean Validation - 수정에 적용

### 64. Bean Validation - 한계

### 65. Bean Validation - groups

66. Form 전송 객체 분리 - 프로젝트 준비 V4

67. Form 전송 객체 분리 - 소개

68. Form 전송 객체 분리 - 개발

### 69. Bean Validation - HTTP 메시지 컨버터

70. 정리
