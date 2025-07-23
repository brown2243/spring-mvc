package com.mvc.hello.web.frontcontroller;

import java.io.IOException;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MyView {
  private String viewPath;

  public void render(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    req.getRequestDispatcher(viewPath).forward(req, resp);
  }

  public void render(Map<String, Object> model, HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    model.forEach((key, value) -> req.setAttribute(key, value));
    req.getRequestDispatcher(viewPath).forward(req, resp);
  }
}
