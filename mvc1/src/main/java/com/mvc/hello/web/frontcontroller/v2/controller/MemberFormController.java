package com.mvc.hello.web.frontcontroller.v2.controller;

import java.io.IOException;

import com.mvc.hello.web.frontcontroller.MyView;
import com.mvc.hello.web.frontcontroller.v2.ControllerV2;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class MemberFormController implements ControllerV2 {
  @Override
  public MyView process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String viewPath = "/WEB-INF/views/new-form.jsp";
    return new MyView(viewPath);
  }
}
