package com.mvc.hello.web.frontcontroller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface MyHandlerAdapter {
  boolean supports(Object handler);

  ModelView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws ServletException, IOException;
}
