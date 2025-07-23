package com.spring.mvc.basic.requestmapping;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/mapping/users")
public class MappingClassController {
  // 회원 목록 조회:GET/users
  // 회원 등록:POST/users
  // 회원 조회:GET/users/{userId}
  // 회원 수정:PATCH/users/{userId}
  // 회원 삭제:DELETE/users/{userId}

  @GetMapping
  public String getUsers() {
    return "getUsers";
  }

  @PostMapping
  public String saveUser() {
    return "saveUser";
  }

  @GetMapping("/{userId}")
  public String getUser(@PathVariable Long userId) {
    return "getUser";
  }

  @PatchMapping("/{userId}")
  public String patchUser(@PathVariable Long userId) {
    return "patchUser";
  }

  @DeleteMapping("/{userId}")
  public String deleteUser(@PathVariable Long userId) {
    return "deleteUser";
  }
}
