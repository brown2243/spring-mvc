# 스프링 MVC 1편 - 백엔드 웹 개발 핵심 기술

## 8. 스프링 MVC - 웹 페이지 만들기

### 60. 프로젝트 생성

### 61. 요구사항 분석

### 62. 상품 도메인 개발

- 개발자는 중복을 줄이려고 노력하지만(DRY) 명확성이 중복보다 중요하다!

### 63. 상품 서비스 HTML

- 이렇게 정적 리소스가 공개되는 /resources/static 폴더에 HTML을 넣어두면, 실제 서비스에서도 공개된다.
- 서비스를 운영한다면 지금처럼 공개할 필요없는 HTML을 두는 것은 주의하자.

### 64. 상품 목록 - 타임리프

- 타임리프는 순수 HTML 파일을 웹 브라우저에서 열어도 내용을 확인할 수 있고, 서버를 통해 뷰 템플릿을 거치면 동적으로 변경된 결과를 확인할 수 있다. JSP를 생각해보면, JSP 파일은 웹 브라우저에서 그냥 열면 JSP 소스코드와 HTML이 뒤죽박죽 되어서 정상적인 확인이 불가능하다. 오직 서버를 통해서 JSP를 열어야 한다.
- 이렇게 순수 HTML을 그대로 유지하면서 뷰 템플릿도 사용할 수 있는 타임리프의 특징을 네츄럴 템플릿(natural templates)이라 한다.

### 65. 상품 상세

### 66. 상품 등록 폼

### 67. 상품 등록 처리 - @ModelAttribute

- @ModelAttribute - 요청 파라미터 처리

  - **@ModelAttribute 는 Item 객체를 생성하고, 요청 파라미터의 값을 프로퍼티 접근법(setXxx)으로 입력해준다.**

- @ModelAttribute - Model 추가

  - @ModelAttribute 는 중요한 한가지 기능이 더 있는데, 바로 모델(Model)에 @ModelAttribute 로 지정한 객체를 자동으로 넣어준다. 지금 코드를 보면 model.addAttribute("item", item) 가 주석처리 되어 있어도 잘 동작하는 것을 확인할 수 있다.

- 모델에 데이터를 담을 때는 이름이 필요하다. 이름은 @ModelAttribute 에 지정한 name(value) 속성을 사용한다.
  - @ModelAttribute("hello") Item item 이름을 hello 로 지정
  - model.addAttribute("hello", item); 모델에 hello 이름으로 저장

### 68. 상품 수정

### 69. PRG Post/Redirect/Get

- 웹 브라우저의 새로 고침은 마지막에 서버에 전송한 데이터를 다시 전송한다.

  - 상품 등록 폼에서 데이터를 입력하고 저장을 선택하면 POST /add + 상품 데이터를 서버로 전송한다.
  - 이 상태에서 새로 고침을 또 선택하면 마지막에 전송한 POST /add + 상품 데이터를 서버로 다시 전송하게 된다.
  - 그래서 내용은 같고, ID만 다른 상품 데이터가 계속 쌓이게 된다.

- 새로 고침 문제를 해결하려면 상품 저장 후에 뷰 템플릿으로 이동하는 것이 아니라, 상품 상세 화면으로 리다이렉트를 호출해주면 된다.
- 웹 브라우저는 리다이렉트의 영향으로 상품 저장 후에 실제 상품 상세 화면으로 다시 이동한다.
- 따라서 마지막에 호출한 내용이 상품 상세 화면인 GET /items/{id} 가 되는 것이다.
- **이런 문제 해결 방식을 PRG Post/Redirect/Get 라 한다.**

- `"redirect:/basic/items/" + item.getId()` redirect에서 +item.getId() 처럼 URL에 변수를 더해서 사용하는 것은 **URL 인코딩이 안되기 때문에 위험**하다.
- 다음에 설명하는 RedirectAttributes 를 사용하자.

### 70. RedirectAttributes

- 리다이렉트 할 때 간단히 status=true 를 추가해보자.

```java
redirectAttributes.addAttribute("itemId", savedItem.getId());
redirectAttributes.addAttribute("status", true);
return "redirect:/basic/items/{itemId}";
// http://localhost:8080/basic/items/3?status=true
```

- RedirectAttributes 를 사용하면 URL 인코딩도 해주고, pathVariable , 쿼리 파라미터까지 처리해준다.
  - pathVariable 바인딩: {itemId}
  - 나머지는 쿼리 파라미터로 처리: ?status=true

### 71. 정리

## 9. 다음으로
