package com.mvc.hello.web.frontcontroller.v1.controller;

import java.io.IOException;

import com.mvc.hello.web.frontcontroller.v1.ControllerV1;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class MemberFormController implements ControllerV1 {
  @Override
  public void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String viewPath = "/WEB-INF/views/new-form.jsp";
    RequestDispatcher requestDispatcher = request.getRequestDispatcher(viewPath);
    requestDispatcher.forward(request, response);
  }
}
