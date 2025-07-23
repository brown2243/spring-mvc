package com.mvc.hello.web.frontcontroller.v3.controller;

import java.util.Map;

import com.mvc.hello.web.frontcontroller.ModelView;
import com.mvc.hello.web.frontcontroller.v3.ControllerV3;

public class MemberFormController implements ControllerV3 {

  @Override
  public ModelView process(Map<String, String> paramMap) {
    String viewPath = "new-form";
    return new ModelView(viewPath);
  }
}
