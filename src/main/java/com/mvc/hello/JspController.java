package com.mvc.hello;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class JspController {

  @GetMapping("/jsp/members/new-form")
  public String form() {
    return "new-form.jsp";
  }

  @PostMapping("/jsp/members/save")
  public String save() {
    return "save.jsp";
  }
}
