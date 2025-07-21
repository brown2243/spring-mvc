package com.mvc.hello.web.frontcontroller.v4.controller;

import java.util.Map;

import com.mvc.hello.web.frontcontroller.v4.ControllerV4;

public class MemberFormControllerV4 implements ControllerV4 {
  @Override
  public String process(Map<String, String> paramMap, Map<String, Object> model) {
    String viewPath = "new-form";
    return viewPath;
  }
}
