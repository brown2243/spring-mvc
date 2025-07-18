package com.mvc.hello.basic.request;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "requestParamServlet", urlPatterns = "/request-param")
public class RequestParamServlet extends HttpServlet {

  @Override
  protected void service(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
    System.out.println("[전체 파라미터 조회] - start");
    /*
     * Enumeration<String> parameterNames = request.getParameterNames();
     * while (parameterNames.hasMoreElements()) {
     * String paramName = parameterNames.nextElement();
     * System.out.println(paramName + "=" +
     * request.getParameter(paramName));
     * }
     */
    request.getParameterNames().asIterator()
        .forEachRemaining(paramName -> System.out.println(paramName +
            "=" + request.getParameter(paramName)));
    System.out.println("[전체 파라미터 조회] - end");
    System.out.println();

    System.out.println("[단일 파라미터 조회]");
    String username = request.getParameter("username");
    System.out.println("request.getParameter(username) = " + username);
    String age = request.getParameter("age");
    System.out.println("request.getParameter(age) = " + age);
    System.out.println();
    System.out.println("[이름이 같은 복수 파라미터 조회]");
    System.out.println("request.getParameterValues(username)");
    String[] usernames = request.getParameterValues("username");
    for (String name : usernames) {
      System.out.println("username=" + name);
    }
    resp.getWriter().write("ok");
  }
}
